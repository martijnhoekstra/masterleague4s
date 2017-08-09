package masterleague4s
package net

import spinoco.fs2.http
import http._
import cats._
import cats.implicits._

sealed trait ClientRunnable[F[_], +A] {
  def run(client: HttpClient[F]): A
}

case class PureRunnable[F[_], A](result: A) extends ClientRunnable[F, A] {
  def run(client: HttpClient[F]) = result
}
case class LiftedRunnable[F[_], A](f: HttpClient[F] => A) extends ClientRunnable[F, A] {
  def run(client: HttpClient[F]) = f(client)
}

object ClientRunnable {
  def lift[F[_], A](f: HttpClient[F] => A): ClientRunnable[F, A] = LiftedRunnable(f)

  def transform[F[_]]: (({ type l[a] = ClientRunnable[F, a] })#l ~> ({ type l[a] = Function1[HttpClient[F], a] })#l) =
    new (({ type l[a] = ClientRunnable[F, a] })#l ~> ({ type l[a] = Function1[HttpClient[F], a] })#l) {
      def apply[A](fa: ClientRunnable[F, A]): HttpClient[F] => A = fa.run _
    }

  class ClientRunnableMonad[F[_]] extends Monad[({ type l[a] = ClientRunnable[F, a] })#l] {

    def flatMap[A, B](fa: ClientRunnable[F, A])(f: A => ClientRunnable[F, B]): ClientRunnable[F, B] = {
      ClientRunnable.lift((client: HttpClient[F]) => f(fa.run(client)).run(client))
    }

    def tailRecM[A, B](a: A)(fn: A => ClientRunnable[F, Either[A, B]]): ClientRunnable[F, B] = {
      val m = cats.instances.all.catsStdMonadReaderForFunction1[HttpClient[F]]
      ClientRunnable.lift(m.tailRecM(a)(fn.map(cr => cr.run(_))))
    }

    def pure[A](a: A): ClientRunnable[F, A] = PureRunnable(a)
  }

  implicit def instances[F[_]] = new ClientRunnableMonad[F]

}