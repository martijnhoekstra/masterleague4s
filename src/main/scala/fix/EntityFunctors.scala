package masterleague4s
package fix

import spinoco.protocol.http.Uri
import java.time.Instant
//import java.time.LocalDate
//import masterleague4s.data._

case class TournamentStage(name: String) //This id == MatchEntry.stage? eliminate
//case class Relation[A, B]?, just Tuple[A, B]?

case class LiveStream(country: String, caster: String, url: Uri, viewers: Int)
case class CalendarMatchF[A, B](datetime: Instant, name: String, format: String, left_team: A, right_team: B)
case class CalendarEntryF[A, B](date: Instant, stage: A, is_live: Boolean, streams: List[LiveStream], matches: List[B])
/*

object Instances {

  sealed trait PlayerHole
  case object TeamPlayerHole
  case object RegionPlayerHole
  case object RolePlayerHole

  sealed trait MatchHole
  case object PatchHole extends MatchHole
  case object TournamentHole extends MatchHole
  case object StageHole extends MatchHole
  case object SeriesHole extends MatchHole
  case object BattlegroundHole extends MatchHole
  case object DraftHole extends MatchHole

}
*/
