package masterleague4s
package data

import java.time.Instant

case class CalendarEntryF[A, B, C](date: Instant,
                                   stage: A,
                                   isLive: Boolean,
                                   streams: List[LiveStream],
                                   matches: List[CalendarMatchF[B, C]])

object CalendarEntryF {
  //todo: Instances
}
