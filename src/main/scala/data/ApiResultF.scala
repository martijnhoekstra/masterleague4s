package masterleague4s
package data

import spinoco.protocol.http.Uri
import cats._
import cats.implicits._

case class APIResultF[A, B](count: Int, next: Option[B], previous: Option[Uri], results: List[A])

object APIResultF {
  implicit def apiResultFunctor: Bitraverse[APIResultF] = new Bitraverse[APIResultF] {
    override def bimap[A, B, C, D](fab: APIResultF[A, B])(f: (A) => C, g: (B) => D): APIResultF[C, D] = {
      val newNext    = fab.next.map(g)
      val newResults = fab.results.map(f)
      fab.copy(next = newNext, results = newResults)
    }

    def bifoldLeft[A, B, C](fab: APIResultF[A, B], c: C)(f: (C, A) => C, g: (C, B) => C): C = {
      val cc: C = fab.results.foldLeft(c)(f)
      fab.next.foldLeft(cc)(g)
    }

    def bifoldRight[A, B, C](fab: APIResultF[A, B], c: Eval[C])(f: (A, Eval[C]) => Eval[C],
                                                                g: (B, Eval[C]) => Eval[C]): Eval[C] = {
      val cc = fab.results.foldRight(c)(f)
      fab.next.foldRight(cc)(g)
    }

    def bitraverse[G[_]: Applicative, A, B, C, D](fab: APIResultF[A, B])(f: A => G[C],
                                                                         g: B => G[D]): G[APIResultF[C, D]] = {
      val goc: G[List[C]]   = fab.results.traverse(f)
      val gld: G[Option[D]] = fab.next.traverse(g)
      for {
        pair <- goc.product(gld)
        (oc, ld) = pair
      } yield (fab.copy(results = oc, next = ld))
    }
  }

}
