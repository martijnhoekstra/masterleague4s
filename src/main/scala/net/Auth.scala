package masterleague4s
package net
package authorization

import spinoco.protocol.http.Uri
import spinoco.protocol.http.Uri.Query
import spinoco.fs2.http.body.BodyEncoder
import spinoco.fs2.http.HttpClient
import spinoco.fs2.http.HttpRequest

import fs2.Stream
import fs2._
import fs2.util.syntax._
import cats.implicits._
import codec.FDecoders._
import codec.CirceSupport._

object Auth {

  def getToken[F[_]: fs2.util.Catchable: fs2.util.Functor](endpoint: Uri, username: String, password: String): ClientRunnable[F, Stream[F, Token]] = {
    implicit val encoder = BodyEncoder.`x-www-form-urlencoded`

    val run: HttpClient[F] => Stream[F, Token] = (client: HttpClient[F]) => for {
      response <- client.request(HttpRequest.post(endpoint, Query.empty :+ ("username" -> username) :+ ("password") -> password))
      token <- Stream.eval(response.bodyAs[Token].map(_.require))
    } yield token

    ClientRunnable.lift(run)

  }

  def authheader(tok: Token): TokenAuthorization = TokenAuthorization(tok)
}

