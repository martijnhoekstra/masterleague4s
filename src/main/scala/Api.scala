package masterleagueapi
package net

object Api {
  import io.circe.Decoder
  import fs2._
  import fs2.util.Catchable
  import spinoco.fs2.http
  import http._
  import masterleagueapi.codec._
  import masterleagueapi.codec.Decoders._
  import fs2.interop.cats._
  import scodec.Attempt
  import spinoco.protocol.http._
  import DefaultResources._
  import scodec.Err
  import cats.implicits._
  import scala.concurrent.duration.FiniteDuration

  def getRequests(res: APIResult[_]): Stream[Task, HttpRequest[Task]] =
    Stream.emits(res.next.toList).map(uri => HttpRequest.get[Task](uri))

  def getEntries[F[_], E <: APIEntry](client: HttpClient[F])(implicit ctch: Catchable[F], decoder: Decoder[E]): HttpRequest[F] => Stream[F, Attempt[APIResult[E]]] =
    r => {
      client.request(r).flatMap { resp =>
        {
          implicit val bodydecoder = CirceSupport.circeDecoder[APIResult[E]](decodeAPICall)
          val fbody = resp.bodyAs[APIResult[E]]
          Stream.eval(fbody)
        }
      }
    }

  def runAPI[A <: APIEntry](uri: Uri, delay: FiniteDuration)(implicit decoder: Decoder[A]): Task[Either[scodec.Err, Map[Long, A]]] = http.client[Task]().flatMap { client =>
    {
      val initialrequest = Attempt.successful(HttpRequest.get[Task](uri)).toEither
      def results2requests(eapiresult: Either[Err, APIResult[A]]): Stream[Task, Either[Err, HttpRequest[Task]]] = eapiresult.traverse(getRequests)
      def reqs2as(ereq: Either[Err, HttpRequest[Task]]): Stream[Task, Either[Err, APIResult[A]]] = ereq match {
        case Right(request) => getEntries(client)(implicitly[Catchable[Task]], decoder)(request).map(_.toEither)
        case Left(err) => Stream.emit(Left(err))
      }

      val fetched = Crawler.crawl(initialrequest, reqs2as, results2requests, time.sleep[Task](delay))

      fetched.runFold(Attempt.successful(Map.empty[Long, A])) {
        case (res, next) => for {
          have <- res
          batch <- Attempt.fromEither(next)
        } yield (have ++ batch.results.map(r => (r.id, r)).toMap)
      }.map(_.toEither)
    }
  }

  def runSingleArray[A <: PlainEntry](uri: Uri)(implicit decoder: Decoder[A]): Task[Either[scodec.Err, Map[Long, A]]] = {
    val tresponsestream = for {
      client <- http.client[Task]()
    } yield client.request(HttpRequest.get[Task](uri))
    implicit val bodydecoder = CirceSupport.circeDecoder[List[A]](decodePlainArray)

    val liststream = for {
      response <- Stream.force(tresponsestream)
      body <- Stream.eval(response.bodyAs[List[A]])
    } yield body

    liststream.runFold(Attempt.successful(Map.empty[Long, A])) {
      case (res, next) => for {
        have <- res
        batch <- next
      } yield (have ++ batch.map(r => (r.id, r)).toMap)
    }.map(_.toEither)

  }

  def matches(wait: FiniteDuration) = runAPI[MatchEntry](Endpoints.matches, wait)
  def heroes(wait: FiniteDuration) = runAPI[HeroEntry](Endpoints.heroes, wait)
  def players(wait: FiniteDuration) = runAPI[PlayerEntry](Endpoints.players, wait)
  def tournaments(wait: FiniteDuration) = runAPI[TournamentEntry](Endpoints.tournaments, wait)
  //val calendar = runAPI[CalendarEntry](Endpoints.calendar)
  def teams(wait: FiniteDuration) = runAPI[TeamEntry](Endpoints.teams, wait)

  val regions = runSingleArray[RegionEntry](Endpoints.regions)
  val patches = runSingleArray[PatchEntry](Endpoints.patches)
  val maps = runSingleArray[MapEntry](Endpoints.maps)

}