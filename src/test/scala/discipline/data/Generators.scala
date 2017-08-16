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

  implicit val arbScheme: Arbitrary[HttpScheme.Value] = Arbitrary(
    Gen.oneOf(HttpScheme.HTTP, HttpScheme.HTTPS, HttpScheme.WS, HttpScheme.WSS))

  implicit val arbHP: Arbitrary[HostPort] = Arbitrary(for {
    host <- arbitrary[String]
    port <- arbitrary[Option[Int]]
  } yield HostPort(host, port))

  implicit val arbPath: Arbitrary[Path] = Arbitrary(for {
    init <- arbitrary[Boolean]
    trailing <- arbitrary[Boolean]
    segments <- arbitrary[List[String]]
  } yield Path(init, trailing, segments))

  implicit val arbQuery: Arbitrary[Query] = Arbitrary(for {
    params <- arbitrary[List[(String, String)]]
    //allows only alphabetically ordered params by key with no duplicate
    //this is not spec-enforced nor generally valid
    //but it is for the use-case
  } yield Query((params.groupBy(_._1).toList.collect { case (_, (k, v) :: _) => (k, v) }).sortBy(_._1)))

  implicit val arbUri: Arbitrary[Uri] = Arbitrary(for {
    scheme <- arbitrary[HttpScheme.Value]
    host <- arbitrary[HostPort]
    path <- arbitrary[Path]
    query <- arbitrary[Query]
  } yield Uri(scheme, host, path, query))

  implicit def arbPortrait: Arbitrary[HeroPortrait] =
    Arbitrary(for {
      small <- arbitrary[Uri]
      medium <- arbitrary[Uri]
    } yield HeroPortrait(small, medium))

  implicit def arbherof[A: Arbitrary]: Arbitrary[HeroF[A]] =
    Arbitrary(for {
      name <- arbitrary[String]
      role <- arbitrary[A]
      url <- arbitrary[Uri]
      portrait <- arbitrary[HeroPortrait]
    } yield HeroF(name, role, url, portrait))

  import masterleague4s.net.APIError

  implicit def arbThrottled: Arbitrary[APIError] =
    Arbitrary(for {
      cause <- arbitrary[String]
    } yield APIError(cause))

  import masterleague4s.net.authorization.Token
  implicit def arbToken: Arbitrary[Token] =
    Arbitrary(for {
      value <- arbitrary[String]
    } yield Token(value))

}
