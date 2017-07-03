package masterleagueapi
package net

import spinoco.protocol.http.Uri

object Endpoints {
  def mljsonuri(resource: String) = Uri.parse(s"https://api.masterleague.net/$resource/?format=json").require

  val heroes = mljsonuri("heroes")
  val matches = mljsonuri("matches")
  val maps = mljsonuri("maps")
  val regions = mljsonuri("regions")
  val patches = mljsonuri("patches")
  val teams = mljsonuri("teams")
  val players = mljsonuri("players")
  val tournaments = mljsonuri("tournaments")
  val calendar = mljsonuri("calendar")
}