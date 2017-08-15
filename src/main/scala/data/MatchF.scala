package masterleague4s
package data

import spinoco.protocol.http.Uri
import java.time.Instant
import cats.implicits._
import cats._

case class MatchF[+A, +B, +C, +D, +E, +F](date: Instant, patch: A, tournament: B, stage: C, round: String, series: D, game: Int, battleground: E, url: Uri, drafts: List[F])

object MatchF {

  def patchHoleFunctor[B, C, D, E, F]: Traverse[({ type l[a] = MatchF[a, B, C, D, E, F] })#l] = new Traverse[({ type l[a] = MatchF[a, B, C, D, E, F] })#l] {
    override def map[A, BB](fa: MatchF[A, B, C, D, E, F])(f: A => BB) = {
      val newPatch = f(fa.patch)
      fa.copy(patch = newPatch)
    }
    def foldLeft[A, BB](fa: MatchF[A, B, C, D, E, F], b: BB)(f: (BB, A) => BB): BB = f(b, fa.patch)
    def foldRight[A, BB](fa: MatchF[A, B, C, D, E, F], lb: Eval[BB])(f: (A, Eval[BB]) => Eval[BB]): Eval[BB] = f(fa.patch, lb)
    def traverse[G[_]: Applicative, A, BB](fa: MatchF[A, B, C, D, E, F])(f: A => G[BB]): G[MatchF[BB, B, C, D, E, F]] = {
      f(fa.patch).map(b => map(fa)(_ => b))
    }
  }

  def tournamentHoleFunctor[A, C, D, E, F]: Traverse[({ type l[a] = MatchF[A, a, C, D, E, F] })#l] = new Traverse[({ type l[a] = MatchF[A, a, C, D, E, F] })#l] {
    override def map[AA, B](fa: MatchF[A, AA, C, D, E, F])(f: AA => B) = {
      val newTournament = f(fa.tournament)
      fa.copy(tournament = newTournament)
    }
    def foldLeft[AA, B](fa: MatchF[A, AA, C, D, E, F], b: B)(f: (B, AA) => B): B = f(b, fa.tournament)
    def foldRight[AA, B](fa: MatchF[A, AA, C, D, E, F], lb: Eval[B])(f: (AA, Eval[B]) => Eval[B]): Eval[B] = f(fa.tournament, lb)
    def traverse[G[_]: Applicative, AA, B](fa: MatchF[A, AA, C, D, E, F])(f: AA => G[B]): G[MatchF[A, B, C, D, E, F]] = {
      f(fa.tournament).map(b => map(fa)(_ => b))
    }
  }

  def stageHoleFunctor[A, B, D, E, F]: Traverse[({ type l[a] = MatchF[A, B, a, D, E, F] })#l] = new Traverse[({ type l[a] = MatchF[A, B, a, D, E, F] })#l] {
    override def map[AA, BB](fa: MatchF[A, B, AA, D, E, F])(f: AA => BB) = {
      val newStage = f(fa.stage)
      fa.copy(stage = newStage)
    }
    def foldLeft[AA, BB](fa: MatchF[A, B, AA, D, E, F], b: BB)(f: (BB, AA) => BB): BB = f(b, fa.stage)
    def foldRight[AA, BB](fa: MatchF[A, B, AA, D, E, F], lb: Eval[BB])(f: (AA, Eval[BB]) => Eval[BB]): Eval[BB] = f(fa.stage, lb)
    def traverse[G[_]: Applicative, AA, BB](fa: MatchF[A, B, AA, D, E, F])(f: AA => G[BB]): G[MatchF[A, B, BB, D, E, F]] = {
      f(fa.stage).map(b => map(fa)(_ => b))
    }
  }

  def seriesHoleFunctor[A, B, C, E, F]: Traverse[({ type l[a] = MatchF[A, B, C, a, E, F] })#l] = new Traverse[({ type l[a] = MatchF[A, B, C, a, E, F] })#l] {
    override def map[AA, BB](fa: MatchF[A, B, C, AA, E, F])(f: AA => BB) = {
      val newSeries = f(fa.series)
      fa.copy(series = newSeries)
    }
    def foldLeft[AA, BB](fa: MatchF[A, B, C, AA, E, F], b: BB)(f: (BB, AA) => BB): BB = f(b, fa.series)
    def foldRight[AA, BB](fa: MatchF[A, B, C, AA, E, F], lb: Eval[BB])(f: (AA, Eval[BB]) => Eval[BB]): Eval[BB] = f(fa.series, lb)
    def traverse[G[_]: Applicative, AA, BB](fa: MatchF[A, B, C, AA, E, F])(f: AA => G[BB]): G[MatchF[A, B, C, BB, E, F]] = {
      f(fa.series).map(b => map(fa)(_ => b))
    }
  }

  def battlegroundHoleFunctor[A, B, C, D, F]: Traverse[({ type l[a] = MatchF[A, B, C, D, a, F] })#l] = new Traverse[({ type l[a] = MatchF[A, B, C, D, a, F] })#l] {
    override def map[AA, BB](fa: MatchF[A, B, C, D, AA, F])(f: AA => BB) = {
      val newMap = f(fa.battleground)
      fa.copy(battleground = newMap)
    }
    def foldLeft[AA, BB](fa: MatchF[A, B, C, D, AA, F], b: BB)(f: (BB, AA) => BB): BB = f(b, fa.battleground)
    def foldRight[AA, BB](fa: MatchF[A, B, C, D, AA, F], lb: Eval[BB])(f: (AA, Eval[BB]) => Eval[BB]): Eval[BB] = f(fa.battleground, lb)
    def traverse[G[_]: Applicative, AA, BB](fa: MatchF[A, B, C, D, AA, F])(f: AA => G[BB]): G[MatchF[A, B, C, D, BB, F]] = {
      f(fa.battleground).map(b => map(fa)(_ => b))
    }
  }

  def draftHoleFunctor[A, B, C, D, E]: Traverse[({ type l[a] = MatchF[A, B, C, D, E, a] })#l] = new Traverse[({ type l[a] = MatchF[A, B, C, D, E, a] })#l] {
    override def map[AA, BB](fa: MatchF[A, B, C, D, E, AA])(f: AA => BB) = {
      val newDrafts = fa.drafts.map(f)
      fa.copy(drafts = newDrafts)
    }

    def foldLeft[AA, BB](fa: MatchF[A, B, C, D, E, AA], b: BB)(f: (BB, AA) => BB): BB = fa.drafts.foldLeft(b)(f)

    def foldRight[AA, BB](fa: MatchF[A, B, C, D, E, AA], lb: Eval[BB])(f: (AA, Eval[BB]) => Eval[BB]): Eval[BB] = fa.drafts.foldRight(lb)(f) //(fa.battleground, lb)

    def traverse[G[_]: Applicative, AA, BB](fa: MatchF[A, B, C, D, E, AA])(f: AA => G[BB]): G[MatchF[A, B, C, D, E, BB]] = {
      fa.drafts.traverse(f).map(newDrafts => fa.copy(drafts = newDrafts))

    }

  }

}