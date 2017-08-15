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
import authorization.Token
import authorization.Auth

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

  def runToMap[F[_]: Async, A: Decoder, K, V](uri: Uri @@ A, tokenprovider: Option[ClientRunnable[F, Stream[F, Token]]])(k: A => K, v: A => V): F[Map[K, V]] = {

    //TODO: Address this wart?
    def trySingle[AA](stream: Stream[F, AA]): F[AA] = runSingle(stream).map {
      case Single(a) => a
      case Empty => throw new Exception("head of empty stream")
      case Multiple => throw new Exception("multiple elements where one was expected")
    }

    sealed trait SingleResult[+AA]
    case object Empty extends SingleResult[Nothing]
    case object Multiple extends SingleResult[Nothing]
    case class Single[AA](a: AA) extends SingleResult[AA]
    object SingleResult {
      def one[AA](a: AA): SingleResult[AA] = Single(a)
      def none[AA]: SingleResult[AA] = Empty
      def multi[AA]: SingleResult[AA] = Multiple
    }

    def runSingle[AA](stream: Stream[F, AA]): F[SingleResult[AA]] = stream.runFold(SingleResult.none[AA]) {
      case (Empty, a) => Single(a)
      case _ => Multiple
    }

    def gatherOption(option: Option[Token]): ClientRunnable[F, F[Map[K, V]]] = for {
      x <- UnfoldApiResult.linearizeApiResult(uri, time.sleep[F](1200.milliseconds), option)
    } yield x.runFold(Map.empty[K, V])((m, page) => {
      //TODO: key and value projections can and should be pushed deeper into the stack
      page.results.foldLeft(m) { case (mm, a) => mm.updated(k(a), v(a)) }
    })

    val runnable = tokenprovider match {
      case None => gatherOption(None)
      case Some(tokenrunnable) => {
        tokenrunnable.flatMap((tokenstream: Stream[F, Token]) => {

          val mappedtokens: Stream[F, ClientRunnable[F, F[Map[K, V]]]] = tokenstream.map(t => gatherOption(Some(t)))

          val firstrunnable: F[ClientRunnable[F, F[Map[K, V]]]] = trySingle(mappedtokens)

          val clientliftable: HttpClient[F] => F[Map[K, V]] = (client) => for {
            runnable <- firstrunnable
            map <- runnable.run(client)
          } yield map

          ClientRunnable.lift(clientliftable)

        })

      }
    }

    val definition = authorization.TokenAuthorization.codec //HeaerCodecDefinition[HttpHeader]
    val mycodecinit = definition.headerCodec //Codec[HttpHeader]
    val next = spinoco.protocol.http.codec.HttpHeaderCodec.codec(4096, otherHeaders = ("Authorization", mycodecinit)) //also Codec[HttpHeader]
    val third = spinoco.protocol.http.codec.HttpRequestHeaderCodec.codec(next) //Codec[HttpRequestHeader]

    val myheadercodec = third

    http.client[F](requestCodec = myheadercodec).flatMap(client => runnable.run(client))

    //And we fail with: Encoding of the header failed: Headers: not a value of type GenericHeader

  }

  import masterleague4s.net.filters._
  import Filtering._

  def authorize[F[_]: Catchable](user: String, pass: String) = Auth.getToken[F](Endpoints.auth, user, pass)

  def allTournaments[F[_]](credentials: Option[(String, String)] = None)(implicit ev: Async[F]) = runToMap(Endpoints.tournaments, credentials.map { case (user, pass) => authorize(user, pass)(ev) })(t => t._1, t => t._2)

  def allMatches[F[_]: Async](credentials: Option[(String, String)] = None) = matchesWhere(MatchFilter.empty, credentials)
  def matchesWhere[F[_]](filter: MatchFilter, credentials: Option[(String, String)] = None)(implicit ev: Async[F]) = runToMap(Endpoints.matches.filter(filter), credentials.map { case (user, pass) => authorize(user, pass)(ev) })(t => t._1, t => t._2)

  def allHeroes[F[_]: Async](credentials: Option[(String, String)] = None) = heroesWhere(HeroFilter.empty, credentials)
  def heroesWhere[F[_]](filter: HeroFilter, credentials: Option[(String, String)] = None)(implicit ev: Async[F]) = runToMap(Endpoints.heroes.filter(filter), credentials.map { case (user, pass) => authorize(user, pass)(ev) })(t => t._1, t => t._2)

  def allPlayers[F[_]](credentials: Option[(String, String)] = None)(implicit ev: Async[F]) = runToMap(Endpoints.players, credentials.map { case (user, pass) => authorize(user, pass)(ev) })(t => t._1, t => t._2)
  def allTeams[F[_]](credentials: Option[(String, String)] = None)(implicit ev: Async[F]) = runToMap(Endpoints.teams, credentials.map { case (user, pass) => authorize(user, pass)(ev) })(t => t._1, t => t._2)

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