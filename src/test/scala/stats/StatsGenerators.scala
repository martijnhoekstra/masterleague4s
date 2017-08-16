package masterleague4s
package stats

import org.scalacheck.Arbitrary
import org.scalacheck.Gen

object StatsGenerators {

  implicit val arbHeroStats: Arbitrary[HeroStats] = Arbitrary {

    for {
      picks <- Gen.posNum[Int]
      wins <- Gen.choose(0, picks)
      bans <- Gen.posNum[Int]
      firstpicks <- Gen.choose(0, picks)
      firstbans <- Gen.choose(0, bans)
      uncontested <- Gen.posNum[Int]
    } yield HeroStats(picks, wins, bans, firstpicks, firstbans, uncontested)
  }

}
