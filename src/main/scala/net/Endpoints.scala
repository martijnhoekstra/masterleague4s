package masterleague4s
package net

import spinoco.protocol.http.Uri
import shapeless.tag
import masterleague4s.data.Serialized._
object Endpoints {
  def mljsonuri(resource: String) = Uri.parse(s"https://api.masterleague.net/$resource/?format=json").require

  val heroes = tag[IdHero][Uri](mljsonuri("heroes"))
  val matches = tag[IdMatch][Uri](mljsonuri("matches"))
  val maps = tag[IdBattleground][Uri](mljsonuri("maps"))
  val regions = tag[IdRegion][Uri](mljsonuri("regions"))
  val patches = tag[IdPatch][Uri](mljsonuri("patches"))
  val teams = tag[IdTeam][Uri](mljsonuri("teams"))
  val players = tag[IdPlayer][Uri](mljsonuri("players"))
  val tournaments = tag[IdTournament][Uri](mljsonuri("tournaments"))
  val calendar = tag[CalendarEntryId][Uri](mljsonuri("calendar"))
}