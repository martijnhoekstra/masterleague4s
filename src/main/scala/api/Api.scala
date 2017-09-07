package masterleague4s
package api

import fs2.util.Catchable
import masterleague4s.codec.FDecoders._
import masterleague4s.net._
import fs2.util.Async

import masterleague4s.net.authorization.Auth
import masterleague4s.net.filters._
import Filtering._
import api.Primitives._

object Api {

  def authorize[F[_]: Catchable](user: String, pass: String) = Auth.getToken[F](Endpoints.auth, user, pass)

  def allTournaments[F[_]](credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runToMap(Endpoints.tournaments, credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t._1, t => t._2)

  def allMatches[F[_]: Async](credentials: Option[(String, String)]) =
    matchesWhere(MatchFilter.empty, credentials)

  def matchesWhere[F[_]](filter: MatchFilter, credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runToMap(Endpoints.matches.filter(filter), credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t._1, t => t._2)

  def allHeroes[F[_]: Async](credentials: Option[(String, String)]) = heroesWhere(HeroFilter.empty, credentials)

  def heroesWhere[F[_]](filter: HeroFilter, credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runToMap(Endpoints.heroes.filter(filter), credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t._1, t => t._2)

  def allPlayers[F[_]](credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runToMap(Endpoints.players, credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t._1, t => t._2)

  def allTeams[F[_]](credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runToMap(Endpoints.teams, credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t._1, t => t._2)

  def fullCalendar[F[_]](credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runToMap(Endpoints.calendar, credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t, t => t)

  def allRegions[F[_]](credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runSingleArray(Endpoints.regions, credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t._1, t => t._2)

  def allPatches[F[_]](credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runSingleArray(Endpoints.patches, credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t._1, t => t._2)

  def allBattlegrounds[F[_]](credentials: Option[(String, String)])(implicit ev: Async[F]) =
    runSingleArray(Endpoints.battlegrounds, credentials.map {
      case (user, pass) => authorize(user, pass)(ev)
    })(t => t._1, t => t._2)

}
