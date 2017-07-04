package masterleagueapi

object Runner {
  import api.Api._
  import scala.concurrent.duration._

  def main(args: Array[String]): Unit = {
    /*
    println("HEROES:")
    (heroes(1.seconds).unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, hero) => s"Hero #$id is ${hero.name}" }
    }) foreach println
    
    println("MATCHES:")
    (matches(1.seconds).unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, matc) => s"Match #$id" }
    }) foreach println

    println("PLAYERS:")
    (players(1.seconds).unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, player) => s"Player #$id is ${player.nickname}" }
    }) foreach println

    println("TOURNAMENTS:")
    (tournaments(1.seconds).unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, tourny) => s"Tournament #$id is ${tourny.name}" }
    }) foreach println

    println("TEAMS:")
    (teams(1.seconds).unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, team) => s"Team #$id is ${team.name}" }
    }) foreach println

    println("REGIONS:")
    (regions.unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, region) => s"Region #$id is ${region.name}" }
    }) foreach println

    println("PATCHES:")
    (patches.unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, patch) => s"Patch #$id is ${patch.name}" }
    }) foreach println

    println("MAPS:")
    (maps.unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, map) => s"Map #$id is ${map.name}" }
    }) foreach println
*/
    import java.time.ZoneOffset
    println("CALENDAR:")
    (calendar(1.seconds).unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(l)) => l.map { entry => s"an event occurs at ${entry.date.atOffset(ZoneOffset.UTC)}" }
    }) foreach println

  }
}