package masterleagueapi
package fix

import spinoco.protocol.http.Uri
import java.time.Instant
import java.time.LocalDate

case class HeroPortrait(small: Uri, medium: Uri)
case class HeroF[A](name: String, role: A, url: Uri, portrait: HeroPortrait)

case class TeamLogo(small: Uri, medium: Uri, large: Uri)
case class TeamEntryF[A](name: String, region: A, url: Uri, logo: TeamLogo)
case class PlayerPhoto(small: Uri, big: Uri, medium: Uri)
case class PlayerF[A, B, C](team: Option[A], region: B, nickname: String, realname: String, country: String, role: C, url: Uri, photo: PlayerPhoto)

case class TournamentStage(name: String) //This id == MatchEntry.stage? eliminate

case class TournamentEntryF[A, B](name: String, start_date: LocalDate, end_date: LocalDate, region: Option[A], url: Uri, stages: List[B])

//case class Relation[A, B]?
case class PickF[A, B](hero: A, player: B)

case class DraftF[A, B, C](team: A, is_winner: Boolean, bans: List[B], picks: List[C])

case class MatchEntryF[A, B, C, D, E, F](date: Instant, patch: A, tournament: B, stage: C, round: String, series: D, game: Int, map: E, url: Uri, drafts: List[F])

/* extends APIEntry {
  def winner = drafts.find(_.is_winner).get
  def loser = drafts.find(d => !d.is_winner).get
  def firstpick = drafts(0)
  def secondpick = drafts(1)
  def firstpickwins = drafts(0) == winner
}*/

case class Map(name: String, url: Uri)
case class Region(name: String)
case class Patch(name: String, from_date: LocalDate, to_date: LocalDate)

case class LiveStream(country: String, caster: String, url: Uri, viewers: Int)
case class CalendarMatchF[A, B](datetime: Instant, name: String, format: String, left_team: A, right_team: B)
case class CalendarEntryF[A, B](date: Instant, stage: A, is_live: Boolean, streams: List[LiveStream], matches: List[B])

sealed trait Role {
  def name: String
}
case object Warrior extends Role {
  val name = "Warrior"
}
case object Support extends Role {
  val name = "Support"
}
case object Assassin extends Role {
  val name = "Assassin"
}
case object Specialist extends Role {
  val name = "Specialist"
}

object Roles {

  val roles = scala.collection.immutable.Map(
    1 -> Warrior,
    2 -> Support,
    3 -> Assassin,
    4 -> Specialist
  )
}

