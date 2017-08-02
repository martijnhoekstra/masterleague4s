package masterleague4s
package api

import io.circe.Decoder
import fs2._
import fs2.util.Catchable
import spinoco.fs2.http
import http._
import masterleague4s.codec._
import masterleague4s.codec.FDecoders._
import fs2.interop.cats._
import scodec.Attempt
import spinoco.protocol.http._
import DefaultResources._
import scodec.Err
import cats.implicits._
import scala.concurrent.duration.FiniteDuration
import masterleague4s.net._
import net.Bridge._
import shapeless.tag.@@
import masterleague4s.data._
import Serialized._
import scala.concurrent.duration._
import fs2.util.Async

object Api {

  def streamAPI[A](uri: Uri @@ A, delay: FiniteDuration)(implicit decoder: Decoder[A]): Stream[Task, Either[Err, UriApiResult[A]]] = {
    type ErrOr[AA] = Either[Err, AA]
    type ErrOrApi[AA] = ErrOr[UriApiResult[AA]]

    val tx: Task[Stream[Task, ErrOrApi[A]]] = http.client[Task]().map { client =>
      {
        def results2uri(eapiresult: ErrOrApi[A]): Stream[Task, ErrOr[Uri @@ A]] = eapiresult.traverse(getRequests)

        def uris2as(ereq: ErrOr[Uri @@ A]): Stream[Task, ErrOrApi[A]] = ereq match {
          case Right(uri) => getEntries(uri)(implicitly[Catchable[Task]], decoder)(client).map(_.toEither)
          case Left(err) => Stream(Left(err))
        }
        Crawler.crawl(Attempt.successful(uri).toEither, uris2as, results2uri, time.sleep[Task](delay))
      }
    }

    Stream.force(tx)
  }

  def streamAPIResults[A: Decoder](uri: Uri @@ A, delay: FiniteDuration): Stream[Task, Either[Err, A]] = streamAPI(uri, delay).flatMap {
    case Right(ar) => Stream.emits(ar.results.map(Right(_)))
    case Left(err) => Stream(Left(err))
  }

  def apiToMap[A: WithId](uri: Uri @@ A, delay: FiniteDuration)(implicit decoder: Decoder[A]): Task[Either[Err, Map[Long, A]]] = {
    val results = streamAPIResults(uri, delay)
    val idprovider = implicitly[WithId[A]]
    results.runFold(Attempt.successful(Map.empty[Long, A])) {
      case (res, next) => for {
        have <- res
        res <- Attempt.fromEither(next)
      } yield have.updated(idprovider.id(res), res)
    }.map(_.toEither)
  }

  def apiToList[A: Decoder](uri: Uri @@ A, delay: FiniteDuration): Task[Either[Err, List[A]]] = {
    val results = streamAPIResults(uri, delay)

    results.runFold(Attempt.successful(List.empty[A])) {
      case (res, next) => for {
        have <- res
        res <- Attempt.fromEither(next)
      } yield res :: have
    }.map(_.toEither)
  }

  def runSingleArray[A: WithId](uri: Uri @@ A)(implicit decoder: Decoder[A]): Task[Either[Err, Map[Long, A]]] = {
    val idprovider = implicitly[WithId[A]]
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
      } yield (have ++ batch.map(r => (idprovider.id(r), r)).toMap)
    }.map(_.toEither)
  }

  def tmapRunnable[F[_]: Async] = for {
    stream <- UnfoldApiResult.linearizeApiResult(Endpoints.tournaments, time.sleep[F](1.seconds))
  } yield stream.runFold(Map.empty[Long, TournamentF[Long, (Long, TournamentStage)]])((m, page) => {
    page.results.foldLeft(m) { case (mm, (l, tourny)) => mm.updated(l, tourny) }
  })

  def tournamentMap[F[_]: Async] = for {
    client <- http.client[F]()
    map <- tmapRunnable.run(client)
  } yield map

  def matches(wait: FiniteDuration) = apiToMap[IdMatch](Endpoints.matches, wait)
  def heroes(wait: FiniteDuration) = apiToMap[IdHero](Endpoints.heroes, wait)
  def players(wait: FiniteDuration) = apiToMap[IdPlayer](Endpoints.players, wait)
  def tournaments(wait: FiniteDuration) = apiToMap[IdTournament](Endpoints.tournaments, wait)
  def calendar(wait: FiniteDuration): Task[Either[Err, List[CalendarEntryId]]] = apiToList[CalendarEntryId](Endpoints.calendar, wait)
  def teams(wait: FiniteDuration) = apiToMap[IdTeam](Endpoints.teams, wait)

  val regions = runSingleArray[IdRegion](Endpoints.regions)
  val patches = runSingleArray[IdPatch](Endpoints.patches)
  val maps = runSingleArray[IdBattleground](Endpoints.maps)

}