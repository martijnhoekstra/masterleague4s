package masterleague4s
package codec

import io.circe._
import spinoco.protocol.http.Uri
import java.time.LocalDate
import java.time.Instant
import scala.util.Try
import shapeless.tag
import shapeless.tag.@@

object FDecoders {
  import data._
  import Serialized._

  implicit val uri: Decoder[Uri] = new Decoder[Uri] {
    final def apply(c: HCursor): Decoder.Result[Uri] =
      for {
        uriString <- c.as[String]
        uri <- Uri.parse(uriString).toEither.left.map(err => DecodingFailure(err.message, c.history))
      } yield uri
  }

  implicit val uriEncoder: Encoder[Uri] = new Encoder[Uri] {
    def apply(uri: Uri) = Json.fromString(uri.toString)
  }

  implicit def uriADecoder[A]: Decoder[Uri @@ A] = uri.map(u => tag[A][Uri](u))
  implicit def uriAEncoder[A]: Encoder[Uri @@ A] = uriEncoder.contramap(id => id)

  implicit val localDate: Decoder[LocalDate] = new Decoder[LocalDate] {
    final def apply(c: HCursor): Decoder.Result[LocalDate] = {
      for {
        dateString <- c.as[String]
        date <- Try(LocalDate.parse(dateString)).toEither.left.map(err => DecodingFailure(err.getMessage + " at " + c.focus.map(_.noSpaces), c.history))
      } yield date
    }
  }

  implicit val instant: Decoder[Instant] = new Decoder[Instant] {
    final def apply(c: HCursor): Decoder.Result[Instant] = {
      for {
        dateString <- c.as[String]
        date <- Try(Instant.parse(dateString)).toEither.left.map(err => DecodingFailure(err.getMessage + " at " + c.focus.map(_.noSpaces), c.history))
      } yield date
    }
  }

  implicit val heroPortaitDecoder: Decoder[HeroPortrait] = Decoder.forProduct2("small", "medium")(HeroPortrait.apply)

  implicit def heroDecoder: Decoder[IdHero] = Decoder.forProduct5("id", "name", "role", "url", "portrait") {
    (id: Long, name: String, role: Long, url: Uri, portrait: HeroPortrait) => (id, HeroF(name, role, url, portrait))
  }

  implicit def battlegroundDecoder: Decoder[IdBattleground] = Decoder.forProduct3("id", "name", "url") {
    (id: Long, name: String, url: Uri) => (id, Battleground(name, url))
  }

  implicit def regionDecoder: Decoder[IdRegion] = Decoder.forProduct2("id", "name") {
    (id: Long, name: String) => (id, Region(name))
  }

  implicit def patchDecoder: Decoder[IdPatch] = Decoder.forProduct4("id", "name", "from_date", "to_date") {
    (id: Long, name: String, fromDate: LocalDate, toDate: LocalDate) => (id, Patch(name, fromDate, toDate))
  }

  implicit def teamLogoDecoder: Decoder[UrlTeamLogo] = Decoder.forProduct3("small", "medium", "big")(TeamLogoF.apply[Uri, Uri, Uri])

  implicit def teamDecoder: Decoder[IdTeam] = Decoder.forProduct5("id", "name", "region", "url", "logo") {
    (id: Long, name: String, region: Long, url: Uri, logo: UrlTeamLogo) => (id, TeamF(name, region, url, logo))
  }

  implicit def playerPhotoDecoder: Decoder[PlayerPhoto] = Decoder.forProduct3("small", "big", "medium")(PlayerPhoto.apply)

  implicit def playerDecoder: Decoder[IdPlayer] = Decoder.forProduct9("id", "team", "region", "nickname", "realname", "country", "role", "url", "photo") {
    (id: Long, team: Option[Long], region: Long, nickname: String, realname: String, country: String, role: Int, url: Uri, photo: PlayerPhoto) =>
      (
        id, //TODO: Fix roles; if a role is added, this'll result in an un-nice pare error
        PlayerF(team, region, nickname, realname, country, Roles.roles(role), url, photo)
      )
  }

  implicit def tournamentStageDecoder: Decoder[IdTournamentStage] = Decoder.forProduct2("id", "name")(
    (id: Long, name: String) => (id, TournamentStage(name))
  )

  implicit def tournamentDecoder: Decoder[IdTournament] = Decoder.forProduct7("id", "name", "start_date", "end_date", "region", "url", "stages") {
    (id: Long, name: String, startDate: LocalDate, endDate: LocalDate, region: Option[Long], url: Uri, stages: List[IdTournamentStage]) => (id, TournamentF(name, startDate, endDate, region, url, stages))
  }

  implicit def pickDecoder: Decoder[IdPick] = Decoder.forProduct2("hero", "player")(PickF.apply[Long, Long])

  implicit def draftDecoder: Decoder[DraftId] = Decoder.forProduct4("team", "is_winner", "bans", "picks")(
    DraftF.apply[Long, Long, IdPick]
  )

  implicit def matchDecoder: Decoder[IdMatch] = Decoder.forProduct11("id", "date", "patch", "tournament", "stage", "round", "series", "game", "map", "url", "drafts") {
    (id: Long, date: Instant, patch: Long, tournament: Long, stage: Long, round: String, series: Long, game: Int, battleground: Long, url: Uri, drafts: List[DraftId]) =>
      (id, MatchF(date, patch, tournament, stage, round, series, game, battleground, url, drafts))
  }

  import net.Throttled
  implicit def throttledDecoder: Decoder[Throttled] = Decoder.forProduct1("detail") {
    (detail: String) => Throttled(detail)
  }
  implicit def throttledEncoder: Encoder[Throttled] = Encoder.forProduct1("detail")(t => t.detail)
  import net.authorization.Token
  implicit def tokenDecoder: Decoder[Token] = Decoder.forProduct1("token") {
    (token: String) => Token(token)
  }
  implicit def tokenEncoder: Encoder[Token] = Encoder.forProduct1("token")(t => t.token)

  implicit def decodeAPICall[A: Decoder]: Decoder[APIResultF[A, Uri @@ A]] = Decoder.forProduct4("count", "next", "previous", "results")(APIResultF.apply[A, Uri @@ A] _)
  implicit def encodeAPICall[A: Encoder]: Encoder[APIResultF[A, Uri @@ A]] = Encoder.forProduct4("count", "next", "previous", "results")(u => (u.count, u.next, u.previous, u.results))

  implicit def decodePlainArray[A: Decoder] = Decoder.decodeList[A]

  implicit val decodeLiveStream: Decoder[LiveStream] = Decoder.forProduct4("country", "caster", "url", "viewers")(LiveStream.apply)
  implicit val decodeCalendarMatch: Decoder[CalendarIdMatch] = Decoder.forProduct5("datetime", "name", "format", "left_team", "right_team")(CalendarMatchF.apply[Long, Long])
  implicit val decodeCalendarEntry: Decoder[CalendarEntryId] = Decoder.forProduct5("date", "stage", "is_live", "streams", "matches")(CalendarEntryF.apply[Long, Long, Long])

}