package masterleagueapi
package net

import spinoco.protocol.http.Uri
import shapeless.tag
import masterleagueapi.codec._
object Endpoints {
  def mljsonuri(resource: String) = Uri.parse(s"https://api.masterleague.net/$resource/?format=json").require

  val heroes = tag[HeroEntry][Uri](mljsonuri("heroes"))
  val matches = tag[MatchEntry][Uri](mljsonuri("matches"))
  val maps = tag[MapEntry][Uri](mljsonuri("maps"))
  val regions = tag[RegionEntry][Uri](mljsonuri("regions"))
  val patches = tag[PatchEntry][Uri](mljsonuri("patches"))
  val teams = tag[TeamEntry][Uri](mljsonuri("teams"))
  val players = tag[PlayerEntry][Uri](mljsonuri("players"))
  val tournaments = tag[TournamentEntry][Uri](mljsonuri("tournaments"))
  val calendar = tag[CalendarEntry][Uri](mljsonuri("calendar"))
}