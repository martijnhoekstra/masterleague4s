package masterleagueapi
package codec

import spinoco.protocol.http.Uri
import java.time.Instant
import java.time.LocalDate

sealed trait APIEntry {
  def id: Long
}

case class HeroPortrait(small: Uri, medium: Uri)
case class HeroEntry(id: Long, name: String, role: Long, url: Uri, portrait: HeroPortrait) extends APIEntry
case class TeamLogo(small: Uri, medium: Uri, large: Uri)
case class TeamEntry(id: Long, name: String, region: Long, url: Uri, logo: TeamLogo) extends APIEntry
case class PlayerPhoto(small: Uri, big: Uri, medium: Uri)
case class PlayerEntry(id: Long, team: Option[Long], region: Long, nickname: String, realname: String, country: String, role: Long, url: Uri, photo: PlayerPhoto) extends APIEntry
case class TournamentStage(id: Long, name: String) //This id == MatchEntry.stage?
case class TournamentEntry(id: Long, name: String, start_date: LocalDate, end_date: LocalDate, region: Option[Long], url: Uri, stages: List[TournamentStage]) extends APIEntry
case class Pick(hero: Long, player: Long)
case class Draft(team: Long, is_winner: Boolean, bans: List[Long], picks: List[Pick])
case class MatchEntry(id: Long, date: Instant, patch: Long, tournament: Long, stage: Long, round: String, series: Long, game: Int, map: Long, url: Uri, drafts: List[Draft]) extends APIEntry {
  def winner = drafts.find(_.is_winner).get
  def loser = drafts.find(d => !d.is_winner).get
  def firstPick = drafts(0)
  def secondPick = drafts(1)
  def firstPickWins = drafts(0) == winner
}
case class MapEntry(id: Long, name: String, url: Uri) extends APIEntry
case class RegionEntry(id: Long, name: String) extends APIEntry
case class PatchEntry(id: Long, name: String, from_date: LocalDate, to_date: LocalDate) extends APIEntry //"2017-05-15"

//Calendar entries have no id, so they can't extend APIEntry
case class LiveStream(country: String, caster: String, url: Uri, viewers: Int)
case class CalendarMatch(datetime: Instant, name: String, format: String, left_team: Long, right_team: Long)
case class CalendarEntry(date: Instant, stage: Long, is_live: Boolean, streams: List[LiveStream], matches: List[CalendarMatch])

