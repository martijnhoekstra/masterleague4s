package masterleague4s
package net

import io.circe.Decoder
import fs2._
import data._
import matryoshka.data.Fix
import shapeless.tag.@@
import spinoco.fs2.http._
import spinoco.protocol.http.Uri
import data.Serialized._
import codec.CirceSupport._
import codec.FDecoders._
import fs2.util.Catchable

//import cats._
//import cats.implicits._

object UnfoldApiResult {
  type StreamRunnable[F[_], A] = ClientRunnable[F, Stream[F, A]]
  type RunnableApiStream[F[_], U, A] = StreamRunnable[F, APIResultF[A, U]]

  type RunnableResult[F[_], A] = Fix[({ type l[a] = RunnableApiStream[F, a, A] })#l]

  def unfoldApiResult[F[_]: Catchable, A: Decoder](uri: Uri @@ A): RunnableResult[F, A] = {
    val d = implicitly[Decoder[A]]
    implicit val bodyDecoder = circeDecoder[UriApiResult[A]](decodeAPICall)
    implicit val c = implicitly[Catchable[F]]
    implicit val f = APIResultF.apiResultFunctor

    def urimap(uriresult: UriApiResult[A]): APIResultF[A, RunnableResult[F, A]] = f.bimap(uriresult)(id => id, uri => unfoldApiResult(uri)(c, d))
    def streammap(str: Stream[F, UriApiResult[A]]): Stream[F, APIResultF[A, RunnableResult[F, A]]] = str.map(urimap)

    val r = HttpRequest.get[F](uri)
    val one: HttpClient[F] => Stream[F, UriApiResult[A]] = (client: HttpClient[F]) => for {
      response <- client.request(r)
      body <- Stream.eval(response.bodyAs[UriApiResult[A]](bodyDecoder, c)).map(_.require)
    } yield body

    val lifted = ClientRunnable.lift(one)
    type Unfix[X] = ClientRunnable[F, Stream[F, APIResultF[A, X]]]

    Fix[Unfix](ClientRunnable.instances.map(lifted)(streammap))
  }

}

object UnfoldApiResult2 {

  def linearizeApiResult[F[_]: Catchable, A: Decoder](uri: Uri @@ A): ClientRunnable[F, Stream[F, APIResultF[A, Unit]]] = {
    val apif = APIResultF.apiResultFunctor
    val init = unfoldApiResult(uri)

    val run = (client: HttpClient[F]) => {

      val unfolded = Stream.unfold(init)((f: UnfoldApiResult.RunnableResult[F, A]) => {
        val runnable = f.unFix
        val pages = runnable.run(client) // sleep here
        val shallowpages = pages.flatMap(page => {
          apif.bimap(page)(id => id, _ => ())
        })
        //we're done if the stream is empty
        //that means running it though
        //that's uncool
        shallowpages.foo
        //need an Option[ShallowPage-or-stream-thereof, Fix[_]]

        ???
      })

      unfolded
    }

    ClientRunnable.lift(run)

  }

  type StreamRunnable[F[_], A] = ClientRunnable[F, Stream[F, A]]
  type RunnableApiStream[F[_], U, A] = StreamRunnable[F, APIResultF[A, U]]

  type RunnableResult[F[_], A] = Fix[({ type l[a] = RunnableApiStream[F, a, A] })#l]

  def unfoldApiResult[F[_]: Catchable, A: Decoder](uri: Uri @@ A): RunnableResult[F, A] = {
    val d = implicitly[Decoder[A]]
    implicit val bodyDecoder = circeDecoder[UriApiResult[A]](decodeAPICall)
    implicit val c = implicitly[Catchable[F]]
    implicit val f = APIResultF.apiResultFunctor

    def urimap(uriresult: UriApiResult[A]): APIResultF[A, RunnableResult[F, A]] = f.bimap(uriresult)(id => id, uri => unfoldApiResult(uri)(c, d))
    def streammap(str: Stream[F, UriApiResult[A]]): Stream[F, APIResultF[A, RunnableResult[F, A]]] = str.map(urimap)

    val r = HttpRequest.get[F](uri)
    val one: HttpClient[F] => Stream[F, UriApiResult[A]] = (client: HttpClient[F]) => for {
      response <- client.request(r)
      body <- Stream.eval(response.bodyAs[UriApiResult[A]](bodyDecoder, c)).map(_.require)
    } yield body

    val lifted = ClientRunnable.lift(one)
    type Unfix[X] = ClientRunnable[F, Stream[F, APIResultF[A, X]]]

    Fix[Unfix](ClientRunnable.instances.map(lifted)(streammap))
  }

}

