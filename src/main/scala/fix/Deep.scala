package masterleague4s
package fix

object Deep {
  import data._
  import Roles.Role
  import spinoco.protocol.http.Uri

  type Hero = HeroF[Role]
  type Team = TeamF[Region, Uri, Uri, Uri]
  type Player = PlayerF[Team, Region, Role]
  type Tournament = TournamentF[Region, TournamentStage]
  type Pick = PickF[Hero, Player]
  type Draft = DraftF[Team, Hero, Hero]
  type Game = MatchF[Patch, Tournament, TournamentStage, Long, Battleground, Draft]
}