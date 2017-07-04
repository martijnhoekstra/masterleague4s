package masterleagueapi
package api

import scala.concurrent.duration._
import scala.collection.JavaConverters._
import fs2.Task
import codec._

object JavaAPI {

  private[this] val wait = 1.seconds
  private[this] def doUnspeakableJavaThingsM[A](t: Task[Either[scodec.Err, Map[Long, A]]]) = t.unsafeRun.fold(
    err => throw new Exception(err.message),
    map => mapAsJavaMap(map)
  )

  def getMatches(): java.util.Map[Long, MatchEntry] = doUnspeakableJavaThingsM(Api.matches(wait))
  def getHeroes(): java.util.Map[Long, HeroEntry] = doUnspeakableJavaThingsM(Api.heroes(wait))
  def getPlayers(): java.util.Map[Long, PlayerEntry] = doUnspeakableJavaThingsM(Api.players(wait))
  def getTournaments(): java.util.Map[Long, TournamentEntry] = doUnspeakableJavaThingsM(Api.tournaments(wait))
  def getCalendar(): java.util.List[CalendarEntry] = Api.calendar(wait).unsafeRun.fold(err => throw new Exception(err.message), list => seqAsJavaList(list))
  def getTeams(): java.util.Map[Long, TeamEntry] = doUnspeakableJavaThingsM(Api.teams(wait))

  def getRegions(): java.util.Map[Long, RegionEntry] = doUnspeakableJavaThingsM(Api.regions)
  def getPatches(): java.util.Map[Long, PatchEntry] = doUnspeakableJavaThingsM(Api.patches)
  def getMaps(): java.util.Map[Long, MapEntry] = doUnspeakableJavaThingsM(Api.maps)
}