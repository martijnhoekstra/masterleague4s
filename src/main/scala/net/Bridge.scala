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

  def getEntries[F[_], E](client: HttpClient[F])(implicit ctch: Catchable[F], decoder: Decoder[E]): Uri @@ E => Stream[F, Attempt[APIResultF[E, Uri @@ E]]] =
    uri => {
      val r = HttpRequest.get[F](uri)

      client.request(r).flatMap { resp =>
        {
          implicit val bodyDecoder = circeDecoder[UriApiResult[E]](decodeAPICall)
          val fBody = resp.bodyAs[UriApiResult[E]]
          Stream.eval(fBody)
        }
      }
    }
}