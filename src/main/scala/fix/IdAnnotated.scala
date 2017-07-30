package masterleague4s
package data

object IdAnnotated {
  import data._
  import Roles.Role
  import spinoco.protocol.http.Uri

  type HeroId = (Long, HeroF[Long])
  type PlayerId = (Long, PlayerF[Long, Long, Role])
  type TournamentEntryId = (Long, TournamentF[Long, Long])
  type PickId = PickF[Long, Long]
  type DraftId = DraftF[Long, Long, PickId]
  type MatchId = (Long, MatchF[Long, Long, Long, Long, Long, DraftId])
  type BattlegroundId = (Long, Battleground)
  type RegionId = (Long, Region)
  type PatchId = (Long, Patch)
  type UrlTeamLogo = TeamLogoF[Uri, Uri, Uri]
  type TeamId = (Long, TeamF[Long, Uri, Uri, Uri])
  type TournamentStageId = (Long, TournamentStage)
  type TournamentId = (Long, TournamentF[Long, TournamentStageId])
  type CalendarEntryId = CalendarEntryF[Long, Long, Long]
  type CalendarMatchId = CalendarMatchF[Long, Long]

  implicit def idforTuple2[A] = new WithId[Tuple2[Long, A]] {
    def id(tup: Tuple2[Long, A]) = tup._1
  }

}

