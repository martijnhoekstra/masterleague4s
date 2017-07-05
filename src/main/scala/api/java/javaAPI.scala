package masterleagueapi
package javaapi

import scala.concurrent.duration._
import scala.collection.JavaConverters._
import fs2.Task
import codec._

object Api {
  import api.{ Api => PlainApi }

  private[this] val wait = 1.seconds
  private[this] def doUnspeakableJavaThingsM[A, B](t: Task[Either[scodec.Err, Map[Long, A]]], projection: A => B) = t.unsafeRun.fold(
    err => throw new Exception(err.message),
    map => mapAsJavaMap(map.map { case (key, value) => (key, projection(value)) })
  )

  def asJavaPick(pick: codec.Pick) = pick match {
    case codec.Pick(hero, player) => javaapi.Pick(hero, player)
  }

  def asJavaStage(stage: codec.TournamentStage) = stage match {
    case codec.TournamentStage(id, name) => javaapi.TournamentStage(id, name)
  }

  def asJavaDraft(draft: codec.Draft) = draft match {
    case codec.Draft(team, is_winner, bans, picks) => javaapi.Draft(team, is_winner, seqAsJavaList(bans), seqAsJavaList(picks.map(asJavaPick)))
  }

  def asJavaMatch(mat: MatchEntry) = mat match {
    case MatchEntry(id, date, patch, tournament, stage, round, series, game, map, _, drafts) => Match(id, date, patch, tournament, stage, round, series, game, map, seqAsJavaList(drafts.map(asJavaDraft)))
  }

  def asJavaHero(hero: HeroEntry) = hero match {
    case HeroEntry(id, name, role, _, _) => Hero(id, name, role)
  }

  def asJavaPlayer(player: PlayerEntry) = player match {
    case PlayerEntry(id, team, region, nickname, realname, country, role, _, _) => Player(id, team, region, nickname, realname, country, role)
  }

  def asJavaTournament(tourny: TournamentEntry) = tourny match {
    case TournamentEntry(id, name, start_date, end_date, region, _, stages) => Tournament(id, name, start_date, end_date, region, seqAsJavaList(stages.map(asJavaStage)))
  }

  def asJavaTeam(team: TeamEntry) = team match {
    case TeamEntry(id, name, region, _, _) => Team(id, name, region)
  }

  def asJavaRegion(region: RegionEntry) = region match {
    case RegionEntry(id, name) => Region(id, name)
  }

  def asJavaPatch(patch: PatchEntry) = patch match {
    case PatchEntry(a, b, c, d) => Patch(a, b, c, d)
  }

  def asJavaGameMap(map: MapEntry) = map match {
    case MapEntry(id, name, _) => GameMap(id, name)
  }

  def asJavaStream(stream: codec.LiveStream) = stream match {
    case codec.LiveStream(country, caster, _, viewers) => javaapi.LiveStream(country, caster, viewers)
  }

  def asJavaCalMatch(m: codec.CalendarMatch) = m match {
    case codec.CalendarMatch(datetime, name, format, left_team, right_team) => javaapi.CalendarMatch(datetime, name, format, left_team, right_team)
  }

  def asJavaCal(calitem: CalendarEntry) = calitem match {
    case CalendarEntry(date, stage, is_live, streams, matches) =>
      CalendarItem(date, stage, is_live, seqAsJavaList(streams.map(asJavaStream)), seqAsJavaList(matches.map(asJavaCalMatch)))
  }

  def getMatches(): java.util.Map[Long, Match] = doUnspeakableJavaThingsM(PlainApi.matches(wait), asJavaMatch)
  def getHeroes(): java.util.Map[Long, Hero] = doUnspeakableJavaThingsM(PlainApi.heroes(wait), asJavaHero)
  def getPlayers(): java.util.Map[Long, Player] = doUnspeakableJavaThingsM(PlainApi.players(wait), asJavaPlayer)
  def getTournaments(): java.util.Map[Long, Tournament] = doUnspeakableJavaThingsM(PlainApi.tournaments(wait), asJavaTournament)
  def getCalendar(): java.util.List[CalendarItem] = PlainApi.calendar(wait).unsafeRun.fold(err => throw new Exception(err.message), list => seqAsJavaList(list.map(asJavaCal)))
  def getTeams(): java.util.Map[Long, Team] = doUnspeakableJavaThingsM(PlainApi.teams(wait), asJavaTeam)

  def getRegions(): java.util.Map[Long, Region] = doUnspeakableJavaThingsM(PlainApi.regions, asJavaRegion)
  def getPatches(): java.util.Map[Long, Patch] = doUnspeakableJavaThingsM(PlainApi.patches, asJavaPatch)
  def getMaps(): java.util.Map[Long, GameMap] = doUnspeakableJavaThingsM(PlainApi.maps, asJavaGameMap)
}