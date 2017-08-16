package masterleague4s
package net

import scala.concurrent.duration.Duration
import scala.util.Try

case class APIError(detail: String)

//model specific errors as extractors. This can't be checked/enforced in a pattern match
//and I'm not sure it's a good idea

case class Throttled(underlying: APIError, retryIn: Duration)
object Throttled {
  def unapply(error: APIError): Option[Throttled] = {
    val pattern = """\d+(\.\d+)? \w+""".r
    for {
      extracted <- pattern.findFirstIn(error.detail)
      duration <- Try(Duration(extracted)).toOption
    } yield Throttled(error, duration)
  }
}

//login error:
/*{"non_field_errors":["Unable to log in with provided credentials."]}*/
