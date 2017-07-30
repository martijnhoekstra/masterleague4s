
package masterleague4s
package javaapi

import scala.concurrent.duration._
import scala.collection.JavaConverters._
import fs2.Task
import masterleague4s.data

import data._
import IdAnnotated._

object Api {
  import api.{ Api => PlainApi }

  private[this] val wait = 1.seconds

  private[this] def doUnspeakableJavaThingsM[A, B](t: Task[Either[scodec.Err, Map[Long, A]]], projection: A => B) = t.unsafeRun.fold(
    err => throw new Exception(err.message),
    map => mapAsJavaMap(map.map { case (key, value) => (key, projection(value)) })
  )

  def asJavaPick(pick: PickF[Long, Long]) = pick match {
    case PickF(hero: Long, player: Long) => javaapi.Pick(hero, player)
  }

  def asJavaStage(stage: TournamentStageId) = stage match {
    case (id, data.TournamentStage(name)) => javaapi.TournamentStage(id, name)
  }

  def asJavaDraft(draft: DraftId) = draft match {
    case DraftF(team: Long, is_winner: Boolean, bans, picks) => javaapi.Draft(team, is_winner, seqAsJavaList(bans), seqAsJavaList(picks.map(asJavaPick)))
  }

  def asJavaMatch(mat: MatchId) = mat match {
    case (id, MatchF(date, patch, tournament, stage, round, series, game, map, _, drafts)) => Match(id, date, patch, tournament, stage, round, series, game, map, seqAsJavaList(drafts.map(asJavaDraft)))
  }

  def asJavaHero(hero: HeroId) = hero match {
    case (id, HeroF(name, role, _, _)) => Hero(id, name, role)
  }

  def asJavaPlayer(player: PlayerId) = player match {
    case (id, PlayerF(team, region, nickname, realname, country, role, _, _)) => Player(id, team, region, nickname, realname, country, Roles.reverseRoles(role))
  }

  def asJavaTournament(tourny: TournamentId) = tourny match {
    case (id, TournamentF(name, start_date, end_date, region, _, stages)) => Tournament(id, name, start_date, end_date, region, seqAsJavaList(stages.map(asJavaStage)))
  }

  def asJavaTeam(team: TeamId) = team match {
    case (id, TeamF(name, region, _, _)) => Team(id, name, region)
  }

  def asJavaRegion(region: RegionId) = region match {
    case (id, data.Region(name)) => Region(id, name)
  }

  def asJavaPatch(patch: PatchId) = patch match {
    case (id, data.Patch(b, c, d)) => Patch(id, b, c, d)
  }

  def asJavaGameMap(map: BattlegroundId) = map match {
    case (id, Battleground(name, _)) => GameMap(id, name)
  }

  def asJavaStream(stream: data.LiveStream) = stream match {
    case data.LiveStream(country, caster, _, viewers) => javaapi.LiveStream(country, caster, viewers)
  }

  def asJavaCalMatch(m: CalendarMatchId) = m match {
    case CalendarMatchF(datetime, name, format, left_team, right_team) => javaapi.CalendarMatch(datetime, name, format, left_team, right_team)
  }

  def asJavaCal(calitem: CalendarEntryId) = calitem match {
    case CalendarEntryF(date, stage, is_live, streams, matches) =>
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
