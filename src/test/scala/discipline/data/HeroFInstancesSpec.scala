package masterleague4s
package instances
package data

import cats.laws.discipline.TraverseTests
import masterleague4s.data.HeroF
import masterleague4s.data.HeroF._
import Generators._
import cats.implicits._

class HeroFInstancesSpec extends DisciplineSpec {
  def is = s2"""
  HeroF[Int] forms a traversable functor       $e1
  """

  def e1 = checkAll("HeroF[Int]", TraverseTests[HeroF].traverse[Int, Long, Byte, Short, List, Vector])
}
