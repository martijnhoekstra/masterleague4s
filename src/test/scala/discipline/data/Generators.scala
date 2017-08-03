package masterleague4s
package instances

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import masterleague4s.data._
import spinoco.protocol.http._
import spinoco.protocol.http.Uri._
//import org.scalacheck.ScalacheckShapeless._

object Generators {

  implicit val arbScheme: Arbitrary[HttpScheme.Value] = Arbitrary(Gen.oneOf(HttpScheme.HTTP, HttpScheme.HTTPS, HttpScheme.WS, HttpScheme.WSS))

  implicit val arbHP: Arbitrary[HostPort] = Arbitrary(for {
    host <- arbitrary[String]
    port <- arbitrary[Option[Int]]
  } yield HostPort(host, port))

  implicit val arbPath: Arbitrary[Path] = Arbitrary(for {
    init <- arbitrary[Boolean]
    trailing <- arbitrary[Boolean]
    segments <- arbitrary[List[String]]
    //Path(initialSlash: Boolean, trailingSlash: Boolean, segments: Seq[String])
  } yield Path(init, trailing, segments))

  implicit val arbQuery: Arbitrary[Query] = Arbitrary(for {
    params <- arbitrary[List[(String, String)]]
  } yield Query(params))

  implicit val arbUri: Arbitrary[Uri] = Arbitrary(for {
    scheme <- arbitrary[HttpScheme.Value]
    host <- arbitrary[HostPort]
    path <- arbitrary[Path]
    query <- arbitrary[Query]
    //Uri(scheme: HttpScheme.Value, host: HostPort, path: Path, query: Query)
  } yield Uri(scheme, host, path, query))

  implicit def arbPortrait: Arbitrary[HeroPortrait] = Arbitrary(for {
    //case class HeroPortrait(small: Uri, medium: Uri)
    small <- arbitrary[Uri]
    medium <- arbitrary[Uri]
  } yield HeroPortrait(small, medium))

  implicit def arbherof[A: Arbitrary]: Arbitrary[HeroF[A]] = Arbitrary(for {
    name <- arbitrary[String]
    role <- arbitrary[A]
    url <- arbitrary[Uri]
    portrait <- arbitrary[HeroPortrait]
  } yield HeroF(name, role, url, portrait))

}

