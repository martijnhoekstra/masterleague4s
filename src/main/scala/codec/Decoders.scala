package masterleagueapi
package codec

object Decoders {
  import io.circe._
  import io.circe.generic.semiauto._
  import spinoco.protocol.http.Uri
  import java.time.LocalDate
  import java.time.Instant
  import scala.util.Try

  implicit val uri: Decoder[Uri] = new Decoder[Uri] {
    final def apply(c: HCursor): Decoder.Result[Uri] =
      for {
        uriString <- c.as[String]
        uri <- Uri.parse(uriString).toEither.left.map(err => DecodingFailure(err.message, c.history))
      } yield uri
  }

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

  implicit val heroPortrait = deriveDecoder[HeroPortrait]
  implicit val heroEntry = deriveDecoder[HeroEntry]
  implicit val mapEntry = deriveDecoder[MapEntry]
  implicit val regionEntry = deriveDecoder[RegionEntry]
  implicit val patchEntry = deriveDecoder[PatchEntry]
  implicit val teamLogo = deriveDecoder[TeamLogo]
  implicit val teamEntry = deriveDecoder[TeamEntry]
  implicit val playerPhoto = deriveDecoder[PlayerPhoto]
  implicit val playerEntry = deriveDecoder[PlayerEntry]
  implicit val tournamentStage = deriveDecoder[TournamentStage]
  implicit val tournamentEntry = deriveDecoder[TournamentEntry]
  implicit val pick = deriveDecoder[Pick]
  implicit val draft = deriveDecoder[Draft]
  implicit val matchEntry = deriveDecoder[MatchEntry]
  implicit val liveStream = deriveDecoder[LiveStream]
  implicit val calendarMatch = deriveDecoder[CalendarMatch]
  implicit val calendarEntry = deriveDecoder[CalendarEntry]

  implicit def decodeAPICall[A: Decoder] = Decoder.forProduct4("count", "next", "previous", "results")(APIResult.apply[A] _)
  implicit def decodePlainArray[A: Decoder] = Decoder.decodeList[A]

}