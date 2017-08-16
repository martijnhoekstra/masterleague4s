package masterleague4s
package net

import spinoco.protocol.http.Uri
import shapeless.tag
import masterleague4s.data.Serialized._

object Endpoints {
  val host = Uri.parse("http://api.masterleague.net/").require

  val auth = Uri.parse("http://api.masterleague.net/auth/token/").require

  val heroes = tag[IdHero][Uri](Uri.parse("https://api.masterleague.net/heroes/?format=json&page_size=25").require)
  val matches = tag[IdMatch][Uri](Uri.parse("https://api.masterleague.net/matches/?format=json&page_size=25").require)
  val battlegrounds = tag[IdBattleground][Uri](Uri.parse("https://api.masterleague.net/maps/?format=json").require)
  val regions = tag[IdRegion][Uri](Uri.parse("https://api.masterleague.net/regions/?format=json").require)
  val teams = tag[IdTeam][Uri](Uri.parse("https://api.masterleague.net/teams/?format=json&page_size=100").require)
  val patches = tag[IdPatch][Uri](Uri.parse("https://api.masterleague.net/patches/?format=json").require)
  val players = tag[IdPlayer][Uri](Uri.parse("https://api.masterleague.net/players/?format=json&page_size=100").require)
  val tournaments = tag[IdTournament][Uri](Uri.parse("https://api.masterleague.net/tournaments/?format=json&page_size=100").require)
  val calendar = tag[CalendarEntryId][Uri](Uri.parse("https://api.masterleague.net/calendar/?format=json").require)

}