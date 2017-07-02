package masterleagueapi
package net

import fs2.Stream
import scala.language.higherKinds

object Crawler {

  def crawl[F[_], A, B](a: A, a2b: A => Stream[F, B], b2a: B => Stream[F, A], sleep: => Stream[F, Unit]): Stream[F, B] = {
    def rec(as: Stream[F, A]): Stream[F, B] =
      as.flatMap { a =>
        a2b(a).flatMap { b =>
          Stream.emit(b) ++ (sleep >> rec(b2a(b)))
        }
      }
    rec(Stream(a))
  }
}