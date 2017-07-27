package masterleagueapi
package api

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
import masterleagueapi.net._
import net.Bridge._

object Api {

  def streamAPI[A](uri: Uri, delay: FiniteDuration)(implicit decoder: Decoder[A]): Stream[Task, Either[Err, APIResult[A]]] = {
    type ErrOr[AA] = Either[Err, AA]
    type ErrOrApi[AA] = ErrOr[APIResult[AA]]

    val tx: Task[Stream[Task, ErrOrApi[A]]] = http.client[Task]().map { client =>
      {
        def results2uri(eapiresult: ErrOrApi[A]): Stream[Task, ErrOr[Uri]] = eapiresult.traverse(getRequests)

        def uris2as(ereq: ErrOr[Uri]): Stream[Task, ErrOrApi[A]] = ereq match {
          case Right(uri) => getEntries(client)(implicitly[Catchable[Task]], decoder)(uri).map(_.toEither)
          case Left(err) => Stream(Left(err))
        }
        Crawler.crawl(Attempt.successful(uri).toEither, uris2as, results2uri, time.sleep[Task](delay))
      }
    }

    Stream.force(tx)
  }

  def streamAPIResults[A](uri: Uri, delay: FiniteDuration)(implicit decoder: Decoder[A]): Stream[Task, Either[Err, A]] = streamAPI(uri, delay)(decoder).flatMap {
    case Right(ar) => Stream.emits(ar.results.map(Right(_)))
    case Left(err) => Stream(Left(err))
  }

  def apiToMap[A <: APIEntry](uri: Uri, delay: FiniteDuration)(implicit decoder: Decoder[A]): Task[Either[Err, Map[Long, A]]] = {
    val results = streamAPIResults(uri, delay)(decoder)
    results.runFold(Attempt.successful(Map.empty[Long, A])) {
      case (res, next) => for {
        have <- res
        res <- Attempt.fromEither(next)
      } yield have.updated(res.id, res)
    }.map(_.toEither)
  }

  def apiToList[A](uri: Uri, delay: FiniteDuration)(implicit decoder: Decoder[A]): Task[Either[Err, List[A]]] = {
    val results = streamAPIResults(uri, delay)(decoder)

    results.runFold(Attempt.successful(List.empty[A])) {
      case (res, next) => for {
        have <- res
        res <- Attempt.fromEither(next)
      } yield res :: have
    }.map(_.toEither)
  }

  def runSingleArray[A <: APIEntry](uri: Uri)(implicit decoder: Decoder[A]): Task[Either[Err, Map[Long, A]]] = {
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

  def matches(wait: FiniteDuration) = apiToMap[MatchEntry](Endpoints.matches, wait)
  def heroes(wait: FiniteDuration) = apiToMap[HeroEntry](Endpoints.heroes, wait)
  def players(wait: FiniteDuration) = apiToMap[PlayerEntry](Endpoints.players, wait)
  def tournaments(wait: FiniteDuration) = apiToMap[TournamentEntry](Endpoints.tournaments, wait)
  def calendar(wait: FiniteDuration): Task[Either[Err, List[CalendarEntry]]] = apiToList[CalendarEntry](Endpoints.calendar, wait)
  def teams(wait: FiniteDuration) = apiToMap[TeamEntry](Endpoints.teams, wait)

  val regions = runSingleArray[RegionEntry](Endpoints.regions)
  val patches = runSingleArray[PatchEntry](Endpoints.patches)
  val maps = runSingleArray[MapEntry](Endpoints.maps)

}