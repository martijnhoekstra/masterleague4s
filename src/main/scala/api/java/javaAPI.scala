package masterleague4s
package javaapi

import scala.collection.JavaConverters._
import fs2.Task
import masterleague4s.data

import data._
import Serialized._

object Api {
  import api.Api._
  import DefaultResources._

  var userpass: Option[(String, String)] = None
  def setCredentials(user: String, pass: String) = {
    userpass = Some((user, pass))
  }

  def clearCredentials() = {
    userpass = None
  }

  private[this] def unsafeRunMap[A, B](t: Task[Map[Long, A]], projection: ((Long, A)) => B): java.util.Map[Long, B] = {
    val smap = t.unsafeRun.map { case (key, value) => (key, projection((key, value))) }
    mapAsJavaMap(smap)
  }

  def asJavaPick(pick: PickF[Long, Long]) = pick match {
    case PickF(hero: Long, player: Long) => javaapi.Pick(hero, player)
  }

  def asJavaStage(stage: IdTournamentStage) = stage match {
    case (id, data.TournamentStage(name)) => javaapi.TournamentStage(id, name)
  }

  def asJavaDraft(draft: DraftId) = draft match {
    case DraftF(team: Long, is_winner: Boolean, bans, picks) =>
      javaapi.Draft(team, is_winner, seqAsJavaList(bans), seqAsJavaList(picks.map(asJavaPick)))
  }

  def asJavaMatch(mat: IdMatch) = mat match {
    case (id, MatchF(date, patch, tournament, stage, round, series, game, map, _, drafts)) =>
      Match(id, date, patch, tournament, stage, round, series, game, map, seqAsJavaList(drafts.map(asJavaDraft)))
  }

  def asJavaHero(hero: IdHero) = hero match {
    case (id, HeroF(name, role, _, _)) => Hero(id, name, role)
  }

  def asJavaPlayer(player: IdPlayer) = player match {
    case (id, PlayerF(team, region, nickname, realname, country, role, _, _)) =>
      Player(id, team, region, nickname, realname, country, Roles.reverseRoles(role))
  }

  def asJavaTournament(tourny: IdTournament) = tourny match {
    case (id, TournamentF(name, start_date, end_date, region, _, stages)) =>
      Tournament(id, name, start_date, end_date, region, seqAsJavaList(stages.map(asJavaStage)))
  }

  def asJavaTeam(team: IdTeam) = team match {
    case (id, TeamF(name, region, _, _)) => Team(id, name, region)
  }

  def asJavaRegion(region: IdRegion) = region match {
    case (id, data.Region(name)) => Region(id, name)
  }

  def asJavaPatch(patch: IdPatch) = patch match {
    case (id, data.Patch(b, c, d)) => Patch(id, b, c, d)
  }

  def asJavaGameMap(map: IdBattleground) = map match {
    case (id, Battleground(name, _)) => GameMap(id, name)
  }

  def asJavaStream(stream: data.LiveStream) = stream match {
    case data.LiveStream(country, caster, _, viewers) => javaapi.LiveStream(country, caster, viewers)
  }

  def asJavaCalMatch(m: CalendarIdMatch) = m match {
    case CalendarMatchF(datetime, name, format, left_team, right_team) =>
      javaapi.CalendarMatch(datetime, name, format, left_team, right_team)
  }

  def asJavaCal(calitem: CalendarEntryId) = calitem match {
    case CalendarEntryF(date, stage, is_live, streams, matches) =>
      CalendarItem(date,
                   stage,
                   is_live,
                   seqAsJavaList(streams.map(asJavaStream)),
                   seqAsJavaList(matches.map(asJavaCalMatch)))
  }

  def getMatches(): java.util.Map[Long, Match]  = unsafeRunMap(allMatches[Task](userpass), asJavaMatch)
  def getHeroes(): java.util.Map[Long, Hero]    = unsafeRunMap(allHeroes[Task](userpass), asJavaHero)
  def getPlayers(): java.util.Map[Long, Player] = unsafeRunMap(allPlayers[Task](userpass), asJavaPlayer)
  def getTeams(): java.util.Map[Long, Team]     = unsafeRunMap(allTeams[Task](userpass), asJavaTeam)
  def getTournaments(): java.util.Map[Long, Tournament] =
    unsafeRunMap(allTournaments[Task](userpass), asJavaTournament)
  /*
  def getCalendar(): java.util.List[CalendarItem] =
    PlainApi
      .fullCalendar(userpass)
      .unsafeRun
      .fold(err => throw new Exception(err.message), list => seqAsJavaList(list.map(asJavaCal)))
   */
  //def getRegions(): java.util.Map[Long, Region] = doUnspeakableJavaThingsM(PlainApi.allRegions[Task], asJavaRegion)
  //def getPatches(): java.util.Map[Long, Patch]  = doUnspeakableJavaThingsM(PlainApi.allPatches[Task], asJavaPatch)
  //def getBattlegrounds(): java.util.Map[Long, GameMap] =
//    doUnspeakableJavaThingsM(PlainApi.allBattlegrounds[Task], asJavaGameMap)

}
