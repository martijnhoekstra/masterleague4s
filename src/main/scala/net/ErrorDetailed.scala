package masterleague4s
package net

import scala.concurrent.duration.Duration
import scala.util.Try

case class Throttled(detail: String) {
  def retryIn: Option[Duration] = {
    //looks like: "Request was throttled. Expected available in 21534.0 seconds."
    val pattern = """\d+(\.\d+)? \w+""".r
    for {
      extracted <- pattern.findFirstIn(detail)
      duration <- Try(Duration(extracted)).toOption
    } yield duration
  }
}