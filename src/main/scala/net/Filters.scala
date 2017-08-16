package masterleague4s
package net
package filters

import spinoco.protocol.http.Uri.Query
import data.Roles._

case class MatchFilter(map: Option[Long], tournament: Option[Long], patch: Option[Long], player: Option[Long])

object MatchFilter {
  def empty = MatchFilter(None, None, None, None)
  implicit def querybuilder = new QueryBuilder[MatchFilter] {
    def query(filter: MatchFilter) =
      Query(
        List("map" -> filter.map,
             "tournament" -> filter.tournament,
             "patch" -> filter.patch,
             "player" -> filter.player)
          .collect { case (name, Some(id)) => (name, id.toString) })
  }
}

case class HeroFilter(role: Option[Role])

object HeroFilter {
  def empty               = HeroFilter(None)
  def forRole(role: Role) = HeroFilter(Some(role))
  implicit def querybuilder = new QueryBuilder[HeroFilter] {
    def query(filter: HeroFilter) =
      Query(filter.role.toList.map {
        case Warrior    => "role" -> "1"
        case Support    => "role" -> "2"
        case Assassin   => "role" -> "3"
        case Specialist => "role" -> "4"
      })
  }
}

case class TeamFilter(region: Option[Long])
object TeamFilter {
  def empty = TeamFilter(None)
  implicit def querybuilder = new QueryBuilder[TeamFilter] {
    def query(filter: TeamFilter) = Query(filter.region.toList.map(l => "region" -> l.toString))
  }
}

object Filtering {
  import net.util.QueryInstances._
  import cats._
  import cats.implicits._
  import shapeless.tag
  import shapeless.tag.@@
  import masterleague4s.data.Serialized._
  import spinoco.protocol.http.Uri

  implicit val mon = net.util.QueryInstances.instances

  def mbuilder = MatchFilter.querybuilder
  def hbuilder = HeroFilter.querybuilder
  def tbuilder = TeamFilter.querybuilder

  def filterMatches(base: Uri @@ IdMatch, filter: MatchFilter) =
    tag[IdMatch][Uri](base.withQuery(base.query |+| mbuilder.query(filter)))

  implicit class MatchFilterOps(receiver: Uri @@ IdMatch) {
    def filter(f: MatchFilter) = filterMatches(receiver, f)
  }

  def filterHeroes(base: Uri @@ IdHero, filter: HeroFilter) =
    tag[IdHero][Uri](base.withQuery(base.query |+| hbuilder.query(filter)))

  implicit class HeroFilterOps(receiver: Uri @@ IdHero) {
    def filter(f: HeroFilter) = filterHeroes(receiver, f)
  }

  def filterTeams(base: Uri @@ IdTeam, filter: TeamFilter) =
    tag[IdTeam][Uri](base.withQuery(base.query |+| tbuilder.query(filter)))

  implicit class TeamFilterOps(receiver: Uri @@ IdTeam) {
    def filter(f: TeamFilter) = filterTeams(receiver, f)
  }
}
