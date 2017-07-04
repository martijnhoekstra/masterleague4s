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

object Bridge {
  def getRequests(res: APIResult[_]): Stream[Task, HttpRequest[Task]] =
    Stream.emits(res.next.toList).map(HttpRequest.get[Task])

  def getEntries[F[_], E](client: HttpClient[F])(implicit ctch: Catchable[F], decoder: Decoder[E]): HttpRequest[F] => Stream[F, Attempt[APIResult[E]]] =
    r => {
      client.request(r).flatMap { resp =>
        {
          implicit val bodydecoder = circeDecoder[APIResult[E]](decodeAPICall)
          val fbody = resp.bodyAs[APIResult[E]]
          Stream.eval(fbody)
        }
      }
    }
}