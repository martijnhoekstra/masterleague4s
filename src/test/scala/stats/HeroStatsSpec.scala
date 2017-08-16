package masterleague4s
package stats

import org.specs2._
//import org.scalacheck.Arbitrary
import StatsGenerators._

class HeroStatsSpec extends Specification with org.specs2.ScalaCheck {

  /*
  case class HeroStats(picks: Int, wins: Int, bans: Int, firstpicks: Int, firstbans: Int, uncontested: Int) {
  def contested = picks + bans
  def total = contested + uncontested
  def winRate = if (picks == 0) None else Some(wins.toDouble / picks)
  def contestedRate = if (total == 0) None else Some(contested.toDouble / total)
  def firstPickRate = if (total == 0) None else Some(firstpicks.toDouble / total)
  def firstBanRate = if (total == 0) None else Some(firstbans.toDouble / total)
  */

  def is = s2"""
  contested     >= picks           $contestedpicks
  contested     >= bans            $contestedbans
  total         >= contested       $totalcontested
  total         >= uncontested     $totaluncontested
  firstpickrate <= contested       $firstpickpick
  firstbanrate  <= contested       $firstbanban
"""

  def contestedpicks = prop((hstats: HeroStats) => {
    hstats.contested must be >= hstats.picks
  })

  def contestedbans = prop((hstats: HeroStats) => {
    hstats.contested must be >= hstats.bans
  })

  def totalcontested = prop((hstats: HeroStats) => {
    hstats.total must be >= hstats.contested
  })

  def totaluncontested = prop((hstats: HeroStats) => {
    hstats.total must be >= hstats.uncontested
  })

  def firstpickpick = prop((hstats: HeroStats) => {
    hstats.firstPickRate must be <= hstats.contestedRate
  })

  def firstbanban = prop((hstats: HeroStats) => {
    hstats.firstBanRate must be <= hstats.contestedRate
  })

}