package masterleague4s
package stats

import masterleague4s.data._

case class HeroStats(picks: Int, wins: Int, bans: Int, firstpicks: Int, firstbans: Int, uncontested: Int) {
  def contested     = picks + bans
  def total         = contested + uncontested
  def winRate       = if (picks == 0) None else Some(wins.toDouble / picks)
  def contestedRate = if (total == 0) None else Some(contested.toDouble / total)
  def firstPickRate = if (total == 0) None else Some(firstpicks.toDouble / total)
  def firstBanRate  = if (total == 0) None else Some(firstbans.toDouble / total)

  def prettypct(d: Double): String = {
    s"${(d * 100).toInt}%"
  }

  def pctOrNa(o: Option[Double]): String = o.map(prettypct).getOrElse("N/A")

  def winLines: List[String] =
    if (picks == 0) List()
    else if (wins == 0) List(s"No wins in $picks games")
    else if (wins == picks) List(s"No losses in $picks games")
    else List(s"$wins/$picks (${prettypct(winRate.get)}) winrate")

  def contestLines: List[String] =
    if (contested == 0) List(s"Uncontested so far for $uncontested matches")
    else
      List(s"$picks picks and $bans bans in $total matches (${pctOrNa(contestedRate)} contested)") :+ {
        if (firstpicks == 0 && firstbans == 0) "none of which as first pick or ban"
        else
          s"with $firstpicks first picks (${pctOrNa(firstPickRate)}) and $firstbans first bans (${pctOrNa(firstBanRate)})"
      }

  def pretty = contestLines ++ winLines
}
object HeroStats {
  def empty = HeroStats(0, 0, 0, 0, 0, 0)
}

object Stats {

  def forHeroInTourny(heroId: Long,
                      tournyId: Long,
                      matches: List[MatchF[_, Long, _, _, _, DraftF[Long, Long, PickF[Long, _]]]]): HeroStats = {
    val matchesInTourny = matches.filter(m => m.tournament == tournyId)
    heroStats(heroId, matchesInTourny)
  }

  def forHeroInPatch(heroId: Long,
                     patchId: Long,
                     matches: List[MatchF[Long, _, _, _, _, DraftF[Long, Long, PickF[Long, _]]]]): HeroStats = {
    val matchesInPatch = matches.filter(m => m.patch == patchId)
    heroStats(heroId, matchesInPatch)
  }

  def heroStats(heroId: Long, matches: List[MatchF[_, _, _, _, _, DraftF[Long, Long, PickF[Long, _]]]]): HeroStats = {
    matches.foldLeft(HeroStats.empty)((stats, m) => {
      val drafts                             = m.drafts
      val (firstdraft :: seconddraft :: Nil) = m.drafts
      val isfirstpick = firstdraft.picks.headOption
        .exists(p => p.hero == heroId) || seconddraft.picks.take(2).exists(p => p.hero == heroId)
      val isfirstban = firstdraft.bans.headOption == Some(heroId) || seconddraft.bans.headOption == Some(heroId)

      val isPick = drafts.flatMap(_.picks).exists(p => p.hero == heroId)
      val isBan  = drafts.flatMap(_.bans).contains(heroId)
      val isWin  = drafts.filter(d => d.isWinner).flatMap(_.picks).exists(p => p.hero == heroId)

      val picks       = stats.picks + { if (isPick) 1 else 0 }
      val wins        = stats.wins + { if (isWin) 1 else 0 }
      val bans        = stats.bans + { if (isBan) 1 else 0 }
      val firstpicks  = stats.firstpicks + { if (isfirstpick) 1 else 0 }
      val firstbans   = stats.firstbans + { if (isfirstban) 1 else 0 }
      val uncontested = stats.uncontested + { if (isPick || isBan) 0 else 1 }
      HeroStats(picks, wins, bans, firstpicks, firstbans, uncontested)
    })
  }
}
