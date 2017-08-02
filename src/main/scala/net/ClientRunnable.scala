package masterleague4s
package net

/*
import io.circe.Decoder

import spinoco.fs2.http
import http._
import codec.CirceSupport._
import codec.FDecoders._
import data._
import fs2.util.Catchable
import spinoco.protocol.http.Uri
import data.Serialized._
import shapeless.tag.@@
import matryoshka.data.Fix
*/
//import fs2._
import spinoco.fs2.http
import http._
import cats._
import cats.implicits._

sealed trait ClientRunnable[F[_], +A] {
  def run(client: HttpClient[F]): A
}

case class PureRunnable[F[_], A](result: A) extends ClientRunnable[F, A] {
  def run(client: HttpClient[F]) = result
}
case class LiftedRunnable[F[_], A](f: HttpClient[F] => A) extends ClientRunnable[F, A] {
  def run(client: HttpClient[F]) = f(client)
}

object ClientRunnable {
  def lift[F[_], A](f: HttpClient[F] => A): ClientRunnable[F, A] = LiftedRunnable(f)

  def transform[F[_]]: (({ type l[a] = ClientRunnable[F, a] })#l ~> ({ type l[a] = Function1[HttpClient[F], a] })#l) =
    new (({ type l[a] = ClientRunnable[F, a] })#l ~> ({ type l[a] = Function1[HttpClient[F], a] })#l) {
      def apply[A](fa: ClientRunnable[F, A]): HttpClient[F] => A = fa.run _
    }

  trait ClientRunnableMonad[F[_]] extends Monad[({ type l[a] = ClientRunnable[F, a] })#l] {

    def flatMap[A, B](fa: ClientRunnable[F, A])(f: A => ClientRunnable[F, B]): ClientRunnable[F, B] = {
      val ffb: HttpClient[F] => B = for {
        a <- fa.run _
        b <- f(a).run _
      } yield b
      ClientRunnable.lift(ffb)
    }

    def tailRecM[A, B](a: A)(fn: A => ClientRunnable[F, Either[A, B]]): ClientRunnable[F, B] = {
      val m = cats.instances.all.catsStdMonadReaderForFunction1[HttpClient[F]]
      ClientRunnable.lift(m.tailRecM(a)(fn.map(cr => cr.run(_))))
    }

    def pure[A](a: A): ClientRunnable[F, A] = PureRunnable(a)
  }

  implicit def instances[F[_]] = new ClientRunnableMonad[F] {}

}
/*
object Foo {

  type RunnableStream[F[_], U, A] = ClientRunnable[F, APIResultF[A, U]]
  type RunnableResult[F[_], A] = Fix[({ type l[a] = RunnableStream[F, a, A] })#l]

  type UriHoleStream[U, A] = Stream[Task, APIResultF[A, U]]
  type ApiResult[A] = Fix[({ type l[a] = UriHoleStream[a, A] })#l]

  def unfoldApiResult[F[_], A: Decoder](uri: Uri @@ A): ApiResult[A] = {
    implicit val bodyDecoder = circeDecoder[UriApiResult[A]](decodeAPICall)
    implicit val c = implicitly[Catchable[Task]]
    implicit val f = APIResultF.apiResultFunctor

    val r = HttpRequest.get[Task](uri)
    val one: Stream[Task, UriApiResult[A]] = for {
      response <- client.request(r)
      body <- Stream.eval(response.bodyAs[UriApiResult[A]](bodyDecoder, c)).map(_.require)

    } yield body

    new Fix[({ type l[a] = UriHoleStream[a, A] })#l](one.map(res => f.bimap(res)(id => id, uri => unfoldApiResult(client, uri))))

  }

}
*/

