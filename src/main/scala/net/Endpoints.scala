package masterleague4s
package net

import spinoco.protocol.http.Uri
import spinoco.protocol.http.Uri._
import masterleague4s.net.util.QueryInstances._
import cats.implicits._
import shapeless.tag
import masterleague4s.data.Serialized._

object Endpoints {
  val host = Uri.parse(s"https://api.masterleague.net/").require
  def json = Query(List("format" -> "json"))
  def maxEntries[A](max: Int) = tag[A][Query](Query(List("page_size" -> max.toString)))

  val maxHeroes = maxEntries[IdHero](20)
  val maxMatches = maxEntries[IdMatch](25)
  val maxTeams = maxEntries[IdTeam](100)
  val maxPlayers = maxEntries[IdPlayer](100)
  val maxTournaments = maxEntries[IdTournament](100)

  def mljsonuri(resource: String) = Uri.parse(s"https://api.masterleague.net/$resource/?format=json").require

  val heroes3 = tag[IdHero][Uri](host.copy(path = Path(true, true, List("heroes"))).withQuery(List(json, maxHeroes).combineAll))

  val heroes = tag[IdHero][Uri](Uri.parse(s"https://api.masterleague.net/heroes/?format=json&page_size=25").require)

  val heroes2 = tag[IdHero][Uri](mljsonuri("heroes"))
  val matches = tag[IdMatch][Uri](mljsonuri("matches"))
  val maps = tag[IdBattleground][Uri](mljsonuri("maps"))
  val regions = tag[IdRegion][Uri](mljsonuri("regions"))
  val patches = tag[IdPatch][Uri](mljsonuri("patches"))
  val teams = tag[IdTeam][Uri](mljsonuri("teams"))
  val players = tag[IdPlayer][Uri](mljsonuri("players"))
  val tournaments = tag[IdTournament][Uri](mljsonuri("tournaments"))
  val calendar = tag[CalendarEntryId][Uri](mljsonuri("calendar"))

  val auth = Uri.parse(s"https://api.masterleague.net/auth/token/").require

}