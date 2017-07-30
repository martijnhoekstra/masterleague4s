package masterleague4s
package data

import spinoco.protocol.http.Uri
import cats.implicits._
import cats._

case class HeroF[A](name: String, role: A, url: Uri, portrait: HeroPortrait)
object HeroF {

  implicit def heroFunctor: Traverse[HeroF] = new Traverse[HeroF] {
    override def map[A, B](fa: HeroF[A])(f: A => B) = {
      val newRole = f(fa.role)
      fa.copy(role = newRole)
    }
    def foldLeft[A, B](fa: HeroF[A], b: B)(f: (B, A) => B): B = f(b, fa.role)
    def foldRight[A, B](fa: HeroF[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = f(fa.role, lb)
    def traverse[G[_]: Applicative, A, B](fa: HeroF[A])(f: A => G[B]): G[HeroF[B]] = f(fa.role).map(b => fa.map(_ => b))
  }
}