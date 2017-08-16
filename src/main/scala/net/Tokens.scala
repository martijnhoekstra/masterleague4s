package masterleague4s
package net
package authorization

import scodec.Codec
import scodec.codecs._

import spinoco.protocol.http.codec.helper._
import spinoco.protocol.http.codec.HttpHeaderCodec
import spinoco.protocol.http.header.HttpHeader
import spinoco.protocol.http.header.Authorization

case class Token(token: String)
case class UserPass(username: String, password: String)

case class TokenAuthorization(credentials: Token) extends HttpHeader {
  //My own authorization header, with hookers and blackjack
  val name = "Authorization"
}

object TokenAuthorization {
  //codec for `Token sometoken`

  val codec: Codec[TokenAuthorization] =
    (asciiConstant("Token") ~> (whitespace() ~> utf8String)).xmap(
      { token =>
        TokenAuthorization(Token(token))
      },
      _.credentials.token
    )

  //codec that first tries `Token sometoken` and then falls back to the known alternatives
  val customAuthorizationHeader: Codec[HttpHeader] = choice(
    codec.upcast[HttpHeader],
    Authorization.codec.headerCodec
  )

  //codec for the full header, including header name
  val headerCodec: Codec[HttpHeader] =
    HttpHeaderCodec.codec(Int.MaxValue, ("Authorization" -> TokenAuthorization.customAuthorizationHeader))

}
