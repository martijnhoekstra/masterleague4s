package masterleagueapi
package fix

import cats.free.Cofree

object IdAnnotated {
  type HeroId = Cofree[HeroF, Long]
  type PlayerId = Cofree[({ type l[A] = PlayerF[Long, Long, Role] })#l, Long]
  type TournamentEntryId = Cofree[({ type l[A] = TournamentEntryF[A, A] })#l, Long]
  type PickId = PickF[Long, Long]
  type DraftId = DraftF[Long, Long, PickId]
  type MatchId = Cofree[({ type l[A] = MatchEntryF[A, A, A, A, A, DraftId] })#l, Long]
}
