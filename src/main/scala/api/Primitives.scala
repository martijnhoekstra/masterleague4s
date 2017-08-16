package masterleague4s
package api

import io.circe.Decoder
import fs2._
import spinoco.fs2.http
import http._
import fs2.interop.cats._
import spinoco.protocol.http._
import DefaultResources._
import cats.implicits._
import masterleague4s.net._
import shapeless.tag.@@
import scala.concurrent.duration._
import fs2.util.Async
import authorization.Token

object Primitives {

  def runToMap[F[_]: Async, A: Decoder, K, V](
      uri: Uri @@ A,
      tokenprovider: Option[ClientRunnable[F, Stream[F, Token]]])(k: A => K, v: A => V): F[Map[K, V]] = {

    //TODO: Address this wart?
    def trySingle[AA](stream: Stream[F, AA]): F[AA] = runSingle(stream).map {
      case Single(a) => a
      case Empty     => throw new Exception("head of empty stream")
      case Multiple  => throw new Exception("multiple elements where one was expected")
    }

    sealed trait SingleResult[+AA]
    case object Empty extends SingleResult[Nothing]
    case object Multiple extends SingleResult[Nothing]
    case class Single[AA](a: AA) extends SingleResult[AA]
    object SingleResult {
      def one[AA](a: AA): SingleResult[AA] = Single(a)
      def none[AA]: SingleResult[AA]       = Empty
      def multi[AA]: SingleResult[AA]      = Multiple
    }

    def runSingle[AA](stream: Stream[F, AA]): F[SingleResult[AA]] = stream.runFold(SingleResult.none[AA]) {
      case (Empty, a) => Single(a)
      case _          => Multiple
    }

    def gatherOption(option: Option[Token]): ClientRunnable[F, F[Map[K, V]]] =
      for {
        x <- UnfoldApiResult.linearizeApiResult(uri, time.sleep[F](1200.milliseconds), option)
      } yield
        x.runFold(Map.empty[K, V])((m, page) => {
          //TODO: key and value projections can and should be pushed deeper into the stack
          page.results.foldLeft(m) { case (mm, a) => mm.updated(k(a), v(a)) }
        })

    val runnable = tokenprovider match {
      case None => gatherOption(None)
      case Some(tokenrunnable) => {
        tokenrunnable.flatMap((tokenstream: Stream[F, Token]) => {

          val mappedtokens: Stream[F, ClientRunnable[F, F[Map[K, V]]]] = tokenstream.map(t => gatherOption(Some(t)))
          val firstrunnable: F[ClientRunnable[F, F[Map[K, V]]]]        = trySingle(mappedtokens)
          val clientliftable: HttpClient[F] => F[Map[K, V]] = (client) =>
            for {
              runnable <- firstrunnable
              map <- runnable.run(client)
            } yield map

          ClientRunnable.lift(clientliftable)
        })
      }
    }

    //oh dear
    http
      .client[F](
        requestCodec =
          spinoco.protocol.http.codec.HttpRequestHeaderCodec.codec(authorization.TokenAuthorization.headerCodec))
      .flatMap(client => runnable.run(client))

  }
}
