package masterleague4s
package net
package filters

import spinoco.protocol.http.Uri.Query
import data.Roles._

case class MatchFilter(map: Option[Long], tournament: Option[Long], patch: Option[Long], player: Option[Long])

object MatchFilter {
  def empty = MatchFilter(None, None, None, None)
  implicit def querybuilder = new QueryBuilder[MatchFilter] {
    def query(filter: MatchFilter) = Query(List("map" -> filter.map, "tournament" -> filter.tournament, "patch" -> filter.patch, "player" -> filter.player)
      .collect { case (name, Some(id)) => (name, id.toString) })
  }
}

case class HeroFilter(role: Option[Role])

object HeroFilter {
  def empty = HeroFilter(None)
  implicit def querybuilder = new QueryBuilder[HeroFilter] {
    def query(filter: HeroFilter) = Query(filter.role.toList.map {
      case Warrior => "role" -> "1"
      case Support => "role" -> "2"
      case Assassin => "role" -> "3"
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
