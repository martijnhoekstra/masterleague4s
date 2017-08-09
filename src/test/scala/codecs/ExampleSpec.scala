package masterleague4s
package codec

import org.specs2._
import org.scalacheck._

class ExampleSpec extends Specification with org.specs2.ScalaCheck {

  def is = s2"""
  a simple property       $ex1
  a more complex property $ex2
"""

  def abStringGen = for {
    ab <- Gen.oneOf("a", "b")
    ab2 <- Gen.oneOf("a", "b")
  } yield (ab + ab2)

  implicit def abStrings: Arbitrary[String] =
    Arbitrary(abStringGen)

  def ex1 = prop((s: String) => s must contain("a") or contain("b")).setArbitrary(abStrings)

  // use the setArbitrary<n> method for the nth argument
  def ex2 = prop((s1: String, s2: String) => (s1 + s2) must contain("a") or contain("b")).
    setArbitrary1(abStrings).setArbitrary2(abStrings)

}