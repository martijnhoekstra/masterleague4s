package masterleague4s
package data

object Serialized {
  import data._
  import Roles.Role
  import spinoco.protocol.http.Uri
  import shapeless.tag.@@

  type IdHero            = (Long, HeroF[Long])
  type IdPlayer          = (Long, PlayerF[Long, Long, Role])
  type IdPick            = PickF[Long, Long]
  type DraftId           = DraftF[Long, Long, IdPick]
  type IdMatch           = (Long, MatchF[Long, Long, Long, Long, Long, DraftId])
  type IdBattleground    = (Long, Battleground)
  type IdRegion          = (Long, Region)
  type IdPatch           = (Long, Patch)
  type UrlTeamLogo       = TeamLogoF[Uri, Uri, Uri]
  type IdTeam            = (Long, TeamF[Long, Uri, Uri, Uri])
  type IdTournamentStage = (Long, TournamentStage)
  type IdTournament      = (Long, TournamentF[Long, IdTournamentStage])
  type CalendarEntryId   = CalendarEntryF[Long, Long, Long]
  type CalendarIdMatch   = CalendarMatchF[Long, Long]
  type UriApiResult[A]   = APIResultF[A, Uri @@ A]

  implicit def idforTuple2[A] = new WithId[Tuple2[Long, A]] {
    def id(tup: Tuple2[Long, A]) = tup._1
  }

}
