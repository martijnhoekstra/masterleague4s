package masterleague4s
package net

import spinoco.protocol.http.Uri
import shapeless.tag
import masterleague4s.data.IdAnnotated._
object Endpoints {
  def mljsonuri(resource: String) = Uri.parse(s"https://api.masterleague.net/$resource/?format=json").require

  val heroes = tag[HeroId][Uri](mljsonuri("heroes"))
  val matches = tag[MatchId][Uri](mljsonuri("matches"))
  val maps = tag[BattlegroundId][Uri](mljsonuri("maps"))
  val regions = tag[RegionId][Uri](mljsonuri("regions"))
  val patches = tag[PatchId][Uri](mljsonuri("patches"))
  val teams = tag[TeamId][Uri](mljsonuri("teams"))
  val players = tag[PlayerId][Uri](mljsonuri("players"))
  val tournaments = tag[TournamentId][Uri](mljsonuri("tournaments"))
  val calendar = tag[CalendarEntryId][Uri](mljsonuri("calendar"))
}