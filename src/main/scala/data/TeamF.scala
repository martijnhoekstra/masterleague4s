package masterleague4s
package data

import spinoco.protocol.http.Uri
import cats.implicits._
import cats._

case class TeamF[A, B, C, D](name: String, region: A, url: Uri, logo: TeamLogoF[B, C, D])

object TeamF {
  implicit def teamFunctor[B, C, D]: Traverse[({ type l[a] = TeamF[a, B, C, D] })#l] =
    new Traverse[({ type l[a] = TeamF[a, B, C, D] })#l] {
      override def map[A, AA](fa: TeamF[A, B, C, D])(f: A => AA) = {
        val newRegion = f(fa.region)
        fa.copy(region = newRegion)
      }
      def foldLeft[A, AA](fa: TeamF[A, B, C, D], b: AA)(f: (AA, A) => AA): AA = f(b, fa.region)
      def foldRight[A, AA](fa: TeamF[A, B, C, D], lb: Eval[AA])(f: (A, Eval[AA]) => Eval[AA]): Eval[AA] =
        f(fa.region, lb)
      def traverse[G[_]: Applicative, A, AA](fa: TeamF[A, B, C, D])(f: A => G[AA]): G[TeamF[AA, B, C, D]] =
        f(fa.region).map(b => fa.copy(region = b))
    }

  //TODO: Add logo functors
}
