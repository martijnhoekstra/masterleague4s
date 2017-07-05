package masterleagueapi
package javaapi

import java.time.Instant
import java.time.LocalDate

case class Hero(id: Long, name: String, role: Long)
case class Team(id: Long, name: String, region: Long)
case class Player(id: Long, team: Option[Long], region: Long, nickname: String, realname: String, country: String, role: Long)
case class TournamentStage(id: Long, name: String) //This id == MatchEntry.stage?
case class Tournament(id: Long, name: String, start_date: LocalDate, end_date: LocalDate, region: Option[Long], stages: java.util.List[TournamentStage])
case class Pick(hero: Long, player: Long)
case class Draft(team: Long, is_winner: Boolean, bans: java.util.List[Long], picks: java.util.List[Pick])
case class Match(id: Long, date: Instant, patch: Long, tournament: Long, stage: Long, round: String, series: Long, game: Int, map: Long, drafts: java.util.List[Draft])
case class GameMap(id: Long, name: String)
case class Region(id: Long, name: String)
case class Patch(id: Long, name: String, from_date: LocalDate, to_date: LocalDate)

//Calendar entries have no id
case class LiveStream(country: String, caster: String, viewers: Int)
case class CalendarMatch(datetime: Instant, name: String, format: String, left_team: Long, right_team: Long)
case class CalendarItem(date: Instant, stage: Long, is_live: Boolean, streams: java.util.List[LiveStream], matches: java.util.List[CalendarMatch])

