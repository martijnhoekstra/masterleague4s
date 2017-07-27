package masterleagueapi
package net

import io.circe.Decoder
import fs2._
import spinoco.fs2.http
import http._
import codec.CirceSupport._
import codec.APIResult
import codec.Decoders._
import scodec.Attempt
import fs2.util.Catchable
import spinoco.protocol.http.Uri
//import shapeless.tag.@@

object Bridge {
  def getRequests(res: APIResult[_]): Stream[Task, Uri] =
    Stream.emits(res.next.toList) //.map(HttpRequest.get[Task])

  def getEntries[F[_], E](client: HttpClient[F])(implicit ctch: Catchable[F], decoder: Decoder[E]): Uri => Stream[F, Attempt[APIResult[E]]] =
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