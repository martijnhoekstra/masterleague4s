package masterleague4s
package instances

import org.specs2.Specification
import org.typelevel.discipline.specs2.Discipline
import org.scalacheck.Arbitrary

trait DisciplineSpec extends Specification with Discipline {
  implicit def arbAndOnly[A](implicit a: A): Arbitrary[A] = Arbitrary(a)
}
