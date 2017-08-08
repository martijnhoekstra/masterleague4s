package masterleague4s
package codec

import io.circe.syntax._
import org.specs2._
//import org.scalacheck._
import masterleague4s.instances.Generators._
import spinoco.protocol.http.Uri

class RoundtripSpec extends Specification with org.specs2.ScalaCheck {

  def is = s2"""
  uri roundtrips from uri $fromUri
  throttled roundtrips $throttled

"""

  import FDecoders._

  def fromUri = prop((u: Uri) => {
    val reparsed = u.asJson.as[Uri]
    reparsed match {
      case Left(_) => 1 must_== 1
      case Right(uri) => uri must_== u
    }
  })

  import masterleague4s.net.Throttled

  def throttled = prop((t: Throttled) => {
    val reparsed = t.asJson.as[Throttled]
    reparsed match {
      case Left(err) => err must_== false
      case Right(parsed) => parsed must_== t
    }
  })

}