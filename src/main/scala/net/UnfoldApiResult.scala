package masterleague4s
package net

import io.circe.Decoder
import fs2._
import data._
import matryoshka.data.Fix
import shapeless.tag.@@
import spinoco.fs2.http._
import spinoco.protocol.http.Uri
//import spinoco.protocol.http.header.Authorization
import data.Serialized._
import codec.CirceSupport._
import codec.FDecoders._
import fs2.util.Catchable
import cats.implicits._
import authorization.Token
//import spinoco.protocol.http.header.value.HttpCredentials.OAuth2BearerToken

object UnfoldApiResult {
  type StreamRunnable[F[_], A]       = ClientRunnable[F, Stream[F, A]]
  type RunnableApiStream[F[_], U, A] = StreamRunnable[F, APIResultF[A, U]]

  type RunnableResult[F[_], A] = Fix[({ type l[a] = RunnableApiStream[F, a, A] })#l]

  def unfoldApiResult[F[_]: Catchable, A: Decoder](uri: Uri @@ A,
                                                   sleep: Stream[F, Unit],
                                                   token: Option[Token]): RunnableResult[F, A] = {
    implicit val bodyDecoder = circeDecoder[UriApiResult[A]](decodeAPICall)

    import spinoco.protocol.http.header.value.ContentType
    import spinoco.protocol.http.header.value.MediaType

    def urimap(uriresult: UriApiResult[A]): APIResultF[A, RunnableResult[F, A]] =
      uriresult.bimap(id => id, uri => unfoldApiResult(uri, sleep, token))
    def streammap(str: Stream[F, UriApiResult[A]]): Stream[F, APIResultF[A, RunnableResult[F, A]]] = str.map(urimap)

    val r: HttpRequest[F] =
      token.foldLeft(HttpRequest.get[F](uri))((req, tok) => req.appendHeader(authorization.Auth.authheader(tok)))

    val one: HttpClient[F] => Stream[F, UriApiResult[A]] = (client: HttpClient[F]) =>
      for {
        response <- (sleep >> client.request(r))
        status = response.header.status
        body <- {
          if (status.isSuccess) Stream.eval(response.bodyAs[UriApiResult[A]]).map(_.require)
          else {
            val textbody =
              Stream.eval(response.withContentType(ContentType(MediaType.`text/plain`, None, None)).bodyAsString)
            textbody.map(body => {
              val error =
                s"Unexpected network response: status ${status.code} - ${status.longDescription} CONTENTS: $body"
              throw new Exception(error)
            })
          }
        }
      } yield body

    val lifted = ClientRunnable.lift(one)
    type Unfix[X] = ClientRunnable[F, Stream[F, APIResultF[A, X]]]

    Fix[Unfix](ClientRunnable.instances.map(lifted)(streammap))
  }

  def linearizeApiResult[F[_]: Catchable, A: Decoder](
      uri: Uri @@ A,
      sleep: Stream[F, Unit],
      token: Option[Token]): ClientRunnable[F, Stream[F, APIResultF[A, Unit]]] = {
    val init = unfoldApiResult(uri, sleep, token)

    val run = (client: HttpClient[F]) => {

      def rec(fix: RunnableResult[F, A]): Stream[F, APIResultF[A, Unit]] = {
        val runnable                              = fix.unFix
        val pages                                 = runnable.run(client)
        val these: Stream[F, APIResultF[A, Unit]] = pages.map(_.bimap(id => id, _ => ()))
        //'t was nice knowing you, stack (TODO: Stack-safety(?))
        val those: Stream[F, APIResultF[A, Unit]] = pages.flatMap(page =>
          page.next match {
            case None    => Stream.empty
            case Some(n) => rec(n)
        })
        these ++ those
      }
      rec(init)
    }
    ClientRunnable.lift(run)
  }
}
