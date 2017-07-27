package masterleagueapi
package fix

object Deep {
  import Roles.Role
  type Hero = HeroF[Role]
  type Team = TeamEntryF[Region]
  type Player = PlayerF[Team, Region, Role]
  type Tournament = TournamentEntryF[Region, TournamentStage]
  type Pick = PickF[Hero, Player]
  type Draft = DraftF[Team, Hero, Hero]
  type Game = MatchEntryF[Patch, Tournament, TournamentStage, Long, Map, Draft]
}