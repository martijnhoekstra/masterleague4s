package masterleague4s
package data

import cats.implicits._
import cats._

case class DraftF[+A, +B, +C](team: A, isWinner: Boolean, bans: List[B], picks: List[C])

object DraftF {
  def teamDraftFunctor[B, C]: Traverse[({ type l[a] = DraftF[a, B, C] })#l] = new Traverse[({ type l[a] = DraftF[a, B, C] })#l] {
    override def map[A, AA](fa: DraftF[A, B, C])(f: A => AA) = {
      val newTeam = f(fa.team)
      fa.copy(team = newTeam)
    }
    def foldLeft[A, AA](fa: DraftF[A, B, C], b: AA)(f: (AA, A) => AA): AA = f(b, fa.team)
    def foldRight[A, AA](fa: DraftF[A, B, C], lb: Eval[AA])(f: (A, Eval[AA]) => Eval[AA]): Eval[AA] = f(fa.team, lb)
    def traverse[G[_]: Applicative, A, AA](fa: DraftF[A, B, C])(f: A => G[AA]): G[DraftF[AA, B, C]] = f(fa.team).map(b => fa.copy(team = b))
  }

  def bansDraftFunctor[A, C]: Traverse[({ type l[a] = DraftF[A, a, C] })#l] = new Traverse[({ type l[a] = DraftF[A, a, C] })#l] {
    override def map[B, BB](fa: DraftF[A, B, C])(f: B => BB) = {
      val newBans = fa.bans.map(f)
      fa.copy(bans = newBans)
    }
    def foldLeft[B, BB](fa: DraftF[A, B, C], b: BB)(f: (BB, B) => BB): BB = fa.bans.foldLeft(b)(f)
    def foldRight[B, BB](fa: DraftF[A, B, C], lb: Eval[BB])(f: (B, Eval[BB]) => Eval[BB]): Eval[BB] = fa.bans.foldRight(lb)(f)
    def traverse[G[_]: Applicative, B, BB](fa: DraftF[A, B, C])(f: B => G[BB]): G[DraftF[A, BB, C]] = fa.bans.traverse(f).map(newBans => fa.copy(bans = newBans))
  }

  def picksDraftFunctor[A, B]: Traverse[({ type l[a] = DraftF[A, B, a] })#l] = new Traverse[({ type l[a] = DraftF[A, B, a] })#l] {
    override def map[C, CC](fa: DraftF[A, B, C])(f: C => CC) = {
      val newPicks = fa.picks.map(f)
      fa.copy(picks = newPicks)
    }
    def foldLeft[C, CC](fa: DraftF[A, B, C], b: CC)(f: (CC, C) => CC): CC = fa.picks.foldLeft(b)(f)
    def foldRight[C, CC](fa: DraftF[A, B, C], lb: Eval[CC])(f: (C, Eval[CC]) => Eval[CC]): Eval[CC] = fa.picks.foldRight(lb)(f)
    def traverse[G[_]: Applicative, C, CC](fa: DraftF[A, B, C])(f: C => G[CC]): G[DraftF[A, B, CC]] = fa.picks.traverse(f).map(newPicks => fa.copy(picks = newPicks))
  }
}