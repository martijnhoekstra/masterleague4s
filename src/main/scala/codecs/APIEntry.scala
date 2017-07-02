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
case class TournamentStage(id: Long, name: String) //what is this ID?
case class TournamentEntry(id: Long, name: String, start_date: LocalDate, end_date: LocalDate, region: Option[Long], url: Uri, stages: List[TournamentStage]) extends APIEntry
case class Pick(hero: Long, player: Long)
case class Draft(team: Long, is_winner: Boolean, bans: List[Long], picks: List[Pick])
case class MatchEntry(id: Long, date: Instant, patch: Long, tournament: Long, stage: Long, round: String, series: Long, game: Int, map: Long, url: Uri, drafts: List[Draft]) extends APIEntry {
  def winner = drafts.find(_.is_winner).get
  def loser = drafts.find(d => !d.is_winner).get
  def firstpick = drafts(0)
  def secondpick = drafts(1)
  def firstpickwins = drafts(0) == winner
}

sealed trait PlainEntry {
  def id: Long
}
//the following endpoint doesn't follow the usual pattern, but are a plain array
case class MapEntry(id: Long, name: String, url: Uri) extends PlainEntry
case class RegionEntry(id: Long, name: String) extends PlainEntry
case class PatchEntry(id: Long, name: String, from_date: LocalDate, to_date: LocalDate) extends PlainEntry //"2017-05-15"
