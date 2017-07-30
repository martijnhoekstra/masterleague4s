package masterleague4s
package data

import spinoco.protocol.http.Uri
import java.time.LocalDate
import cats.implicits._
import cats._

case class TournamentF[A, B](name: String, startDate: LocalDate, endDate: LocalDate, region: Option[A], url: Uri, stages: List[B])

object TournamentF {
  implicit def tournementBiFunctor: Bitraverse[TournamentF] = new Bitraverse[TournamentF] {
    override def bimap[A, B, C, D](fab: TournamentF[A, B])(f: (A) => C, g: (B) => D): TournamentF[C, D] = {
      val newRegion = fab.region.map(f)
      val newStages = fab.stages.map(g)
      fab.copy(region = newRegion, stages = newStages)
    }
    def bifoldLeft[A, B, C](fab: TournamentF[A, B], c: C)(f: (C, A) => C, g: (C, B) => C): C = {
      val fed: Option[C] = fab.region.map(r => f(c, r))
      fed match {
        case None => fab.stages.foldLeft(c)(g)
        case Some(cc) => fab.stages.foldLeft(cc)(g)
      }
    }

    def bifoldRight[A, B, C](fab: TournamentF[A, B], c: Eval[C])(f: (A, Eval[C]) => Eval[C], g: (B, Eval[C]) => Eval[C]): Eval[C] = {
      val fed = fab.region.map(r => f(r, c))
      fed match {
        case None => fab.stages.foldRight(c)(g)
        case Some(cc) => fab.stages.foldRight(cc)(g)
      }
    }

    def bitraverse[G[_]: Applicative, A, B, C, D](fab: TournamentF[A, B])(f: A => G[C], g: B => G[D]): G[TournamentF[C, D]] = {
      val goc: G[Option[C]] = fab.region.traverse(f)
      val gld: G[List[D]] = fab.stages.traverse(g)
      for {
        pair <- goc.product(gld)
        (oc, ld) = pair
      } yield (fab.copy(region = oc, stages = ld))
    }
  }
}