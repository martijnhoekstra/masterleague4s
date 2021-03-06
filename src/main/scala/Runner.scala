package masterleague4s

object Runner {
  //import api.Api._
  //import fs2.Task
  //import DefaultResources._
  import com.typesafe.config.Config
  import com.typesafe.config.ConfigFactory

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    def getCredentials(config: Config) =
      if (config.hasPath("credentials")) {
        val credConfig = config.getConfig("credentials")
        if (credConfig.hasPath("user") && credConfig.hasPath("pass"))
          Some((credConfig.getString("user"), credConfig.getString("pass")))
        else None
      } else None

    val ml4sconfig  = config.getConfig("masterleague4s")
    val credentials = getCredentials(ml4sconfig)

    println(credentials)

    //import spinoco.protocol.http
    //import spinoco.protocol.http.Uri
    //import spinoco.protocol.http.Uri.Query
    //import spinoco.fs2.http.body.BodyEncoder

    //import spinoco.fs2.http
    //import spinoco.fs2.http.HttpClient
    //import spinoco.fs2.http.HttpRequest
    //import fs2.Stream
    //import masterleague4s.net.authorization.Auth
    /*

    val tr = for {
      client <- spinoco.fs2.http.client[Task]()
      resp <- runnable.run(client).runLogFree.run
    } yield resp

    tr.unsafeAttemptRun match {
      case Left(err) => println(s"error: $err")
      case Right(v) => println({ v.head })
    }

    println("TOURNAMENTS:")
    (allTournaments[Task].unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(m) => m.toList.map { case (id, tourny) => s"Tournament #$id is ${tourny.name}" }
    }) foreach println


    println("MATCHES:")
    (allMatches[Task].unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(m) => m.toList.map { case (id, _) => s"Match #$id" }
    }) foreach println
    println("PLAYERS:")
    (allPlayers[Task].unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(m) => m.toList.map { case (id, player) => s"Player #$id is ${player.nickname}" }
    }) foreach println

    val matchlist = allMatches[Task](credentials).unsafeAttemptRun match {
      case Left(err) => { println(err); List() }
      case Right(m) => m.toList.map(_._2)
    }

    println("ETC in the current patch")

    val patchstats = masterleague4s.stats.Stats.forHeroInPatch(21l, 24l, matchlist)
    patchstats.pretty.foreach(println)

    println("ETC in HGC Europe - Open Division")
    val tournystatsstats = masterleague4s.stats.Stats.forHeroInTourny(21l, 35l, matchlist)
    tournystatsstats.pretty.foreach(println)


    println("HEROES (Specialists):")
    import masterleague4s.net.filters._
    import masterleague4s.data.Roles._
    val h = heroesWhere[Task](HeroFilter.forRole(Specialist), credentials).unsafeAttemptRun match {
      case Left(err) => {
        err.printStackTrace
        List(s"error: ${err}")
      }
      case Right(m) => m.toList.map { case (id, hero) => s"Hero #$id is ${hero.name}" }
    }

    h foreach println

    println("TEAMS:")
    (allTeams[Task](credentials).unsafeAttemptRun match {
      case Left(err) => {
        err.printStackTrace
        List(s"error: ${err}")
      }
      case Right(m) => m.toList.map { case (id, team) => s"Team #$id is ${team.name}" }
    }) foreach println



    println("REGIONS:")
    (regions.unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, region) => s"Region #$id is ${region._2.name}" }
    }) foreach println

    println("PATCHES:")
    (patches.unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, patch) => s"Patch #$id is ${patch._2.name}" }
    }) foreach println

    println("MAPS:")
    (maps.unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(m)) => m.toList.map { case (id, map) => s"Map #$id is ${map._2.name}" }
    }) foreach println

    import java.time.ZoneOffset
    println("CALENDAR:")
    (calendar(1.seconds).unsafeAttemptRun match {
      case Left(err) => List(s"error: $err")
      case Right(Left(thr)) => List(s"error: $thr")
      case Right(Right(l)) => l.map { entry => s"an event occurs at ${entry.date.atOffset(ZoneOffset.UTC)}" }
    }) foreach println
   */
  }
}
