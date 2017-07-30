package masterleague4s
package net

import io.circe.Decoder
import fs2._
import spinoco.fs2.http
import http._
import codec.CirceSupport._
import codec.APIResult
import codec.FDecoders._
import scodec.Attempt
import fs2.util.Catchable
import spinoco.protocol.http.Uri
import shapeless.tag
import shapeless.tag.@@

object Bridge {
  def getRequests[A](res: APIResult[A]): Stream[Task, Uri @@ A] =
    Stream.emits(res.next.toList.map(tag[A][Uri]))

  def getEntries[F[_], E](client: HttpClient[F])(implicit ctch: Catchable[F], decoder: Decoder[E]): Uri @@ E => Stream[F, Attempt[APIResult[E]]] =
    uri => {
      val r = HttpRequest.get[F](uri)

      client.request(r).flatMap { resp =>
        {
          implicit val bodyDecoder = circeDecoder[APIResult[E]](decodeAPICall)
          val fBody = resp.bodyAs[APIResult[E]]
          Stream.eval(fBody)
        }
      }
    }
}