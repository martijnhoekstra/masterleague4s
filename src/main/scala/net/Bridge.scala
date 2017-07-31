package masterleague4s
package net

import io.circe.Decoder
import fs2._
import spinoco.fs2.http
import http._
import codec.CirceSupport._
import codec.FDecoders._
import data._
import scodec.Attempt
import fs2.util.Catchable
import spinoco.protocol.http.Uri
import data.Serialized._
import shapeless.tag.@@

object Bridge {
  def getRequests[A](res: UriApiResult[A]): Stream[Task, Uri @@ A] =
    Stream.emits(res.next.toList)

  def getEntries[F[_], E](uri: Uri @@ E)(implicit ctch: Catchable[F], decoder: Decoder[E]): HttpClient[F] => Stream[F, Attempt[APIResultF[E, Uri @@ E]]] =
    client => {
      val r = HttpRequest.get[F](uri)

      client.request(r).flatMap { resp =>
        {
          implicit val bodyDecoder = circeDecoder[UriApiResult[E]](decodeAPICall)
          val fBody = resp.bodyAs[UriApiResult[E]]
          Stream.eval(fBody)
        }
      }
    }

  import matryoshka.data.Fix
  type UriHoleStream[U, A] = Stream[Task, APIResultF[A, U]]
  type ApiResult[A] = Fix[({ type l[a] = UriHoleStream[a, A] })#l]

  def unfoldApiResult[A: Decoder](client: HttpClient[Task], uri: Uri @@ A): ApiResult[A] = {
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