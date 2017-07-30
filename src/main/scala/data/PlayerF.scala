package masterleague4s
package data

import spinoco.protocol.http.Uri
import cats.implicits._
import cats._

case class PlayerF[A, B, C](team: Option[A], region: B, nickname: String, realname: String, country: String, role: C, url: Uri, photo: PlayerPhoto)

object PlayerF {

  def teamPlayerHoleFunctor[B, C]: Traverse[({ type l[a] = PlayerF[a, B, C] })#l] = new Traverse[({ type l[a] = PlayerF[a, B, C] })#l] {
    override def map[A, AA](fa: PlayerF[A, B, C])(f: A => AA) = {
      val newTeam = fa.team.map(f)
      fa.copy(team = newTeam)
    }

    def foldLeft[A, BB](fa: PlayerF[A, B, C], b: BB)(f: (BB, A) => BB): BB = fa.team.foldLeft(b)(f)
    def foldRight[A, BB](fa: PlayerF[A, B, C], lb: Eval[BB])(f: (A, Eval[BB]) => Eval[BB]): Eval[BB] = fa.team.foldRight(lb)(f)
    def traverse[G[_]: Applicative, A, AA](fa: PlayerF[A, B, C])(f: A => G[AA]): G[PlayerF[AA, B, C]] = {
      fa.team.traverse(f).map(newTeam => fa.copy(team = newTeam))
    }
  }

  def regionPlayerHoleFunctor[A, C]: Traverse[({ type l[a] = PlayerF[A, a, C] })#l] = new Traverse[({ type l[a] = PlayerF[A, a, C] })#l] {
    override def map[B, BB](fa: PlayerF[A, B, C])(f: B => BB) = {
      val newRegion = f(fa.region)
      fa.copy(region = newRegion)
    }

    def foldLeft[B, BB](fa: PlayerF[A, B, C], b: BB)(f: (BB, B) => BB): BB = f(b, fa.region)
    def foldRight[B, BB](fa: PlayerF[A, B, C], lb: Eval[BB])(f: (B, Eval[BB]) => Eval[BB]): Eval[BB] = f(fa.region, lb)
    def traverse[G[_]: Applicative, B, BB](fa: PlayerF[A, B, C])(f: B => G[BB]): G[PlayerF[A, BB, C]] = f(fa.region).map(b => fa.copy(region = b))
  }

  def rolePlayerHoleFunctor[A, B]: Traverse[({ type l[a] = PlayerF[A, B, a] })#l] = new Traverse[({ type l[a] = PlayerF[A, B, a] })#l] {
    override def map[C, CC](fa: PlayerF[A, B, C])(f: C => CC) = {
      val newRole = f(fa.role)
      fa.copy(role = newRole)
    }

    def foldLeft[C, CC](fa: PlayerF[A, B, C], b: CC)(f: (CC, C) => CC): CC = f(b, fa.role)
    def foldRight[C, CC](fa: PlayerF[A, B, C], lb: Eval[CC])(f: (C, Eval[CC]) => Eval[CC]): Eval[CC] = f(fa.role, lb)
    def traverse[G[_]: Applicative, C, CC](fa: PlayerF[A, B, C])(f: C => G[CC]): G[PlayerF[A, B, CC]] = {
      f(fa.role).map(b => fa.copy(role = b))
    }
  }
}