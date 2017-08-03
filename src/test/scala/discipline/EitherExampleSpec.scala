package masterleague4s
package instances

import cats.implicits._
import cats.laws.discipline.FunctorTests

class EitherExampleSpec extends DisciplineSpec {
  def is = s2"""
  Either[Int, ?] forms a functor                           $e1
  """

  def e1 = checkAll("Either[Int, Int]", FunctorTests[({ type l[a] = Either[Int, a] })#l].functor[Int, Int, Int])
}