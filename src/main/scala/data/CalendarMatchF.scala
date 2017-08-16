package masterleague4s
package data

import java.time.Instant

case class CalendarMatchF[A, B](datetime: Instant, name: String, format: String, leftTeam: A, rightTeam: B)

object CalendarMatchF {
  //TODO: Bifunctor instance

}
