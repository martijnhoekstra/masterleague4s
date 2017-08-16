package masterleague4s
package codec

object CirceSupport {
  import io.circe._
  import scodec.{Attempt, Err}
  import spinoco.protocol.http.header.value.HttpCharset
  import spinoco.fs2.http.body.BodyDecoder
  import io.circe.parser._

  implicit def circeDecoder[A: Decoder]: BodyDecoder[A] = BodyDecoder.apply[A] {
    case (bytes, ct) =>
      HttpCharset.asJavaCharset(ct.charset.getOrElse(HttpCharset.`UTF-8`)).flatMap { implicit chs =>
        {
          val es = bytes.decodeString
          val x = for {
            s <- es
            json <- parse(s)
            a <- json.as[A]
          } yield a
          Attempt.fromEither(x.left.map(ex =>
            Err(s"Failed to decode string $es ContentType: $ct, charset: $chs, err: ${ex.getMessage}")))
        }
      }
  }
}
