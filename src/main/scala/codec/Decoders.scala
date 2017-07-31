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

  implicit def uria[A]: Decoder[Uri @@ A] = uri.map(u => tag[A][Uri](u))

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

  implicit def decodeAPICall[A: Decoder] = Decoder.forProduct4("count", "next", "previous", "results")(APIResultF.apply[A, Uri @@ A] _)
  implicit def decodePlainArray[A: Decoder] = Decoder.decodeList[A]

  implicit val decodeLiveStream: Decoder[LiveStream] = Decoder.forProduct4("country", "caster", "url", "viewers")(LiveStream.apply)
  implicit val decodeCalendarMatch: Decoder[CalendarIdMatch] = Decoder.forProduct5("datetime", "name", "format", "left_team", "right_team")(CalendarMatchF.apply[Long, Long])
  implicit val decodeCalendarEntry: Decoder[CalendarEntryId] = Decoder.forProduct5("date", "stage", "is_live", "streams", "matches")(CalendarEntryF.apply[Long, Long, Long])

  //import Decoders._
  //import data._

  /*
  implicit def heroDecoder = heroEntry.map(he => (he.id, HeroF(he.name, he.role, he.url, he.portrait)))
  implicit def teamDecoder = teamEntry.map(te => (te.id, TeamF(te.name, te.region, te.url, te.logo)))
  implicit def playerDecoder = playerEntry.map(pe => (pe.id, PlayerF(pe.team, pe.region, pe.nickname, pe.realname, pe.country, pe.role, pe.url, pe.photo)))
  */
  /*

case class TournamentStage(id: Long, name: String) //This id == MatchEntry.stage?
case class TournamentEntry(id: Long, name: String, start_date: LocalDate, end_date: LocalDate, region: Option[Long], url: Uri, stages: List[TournamentStage]) extends APIEntry
case class Pick(hero: Long, player: Long)
case class Draft(team: Long, is_winner: Boolean, bans: List[Long], picks: List[Pick])
case class MatchEntry(id: Long, date: Instant, patch: Long, tournament: Long, stage: Long, round: String, series: Long, game: Int, map: Long, url: Uri, drafts: List[Draft]) extends APIEntry {
  def winner = drafts.find(_.is_winner).get
  def loser = drafts.find(d => !d.is_winner).get
  def firstPick = drafts(0)
  def secondPick = drafts(1)
  def firstPickWins = drafts(0) == winner
}
case class MapEntry(id: Long, name: String, url: Uri) extends APIEntry
case class RegionEntry(id: Long, name: String) extends APIEntry
case class PatchEntry(id: Long, name: String, from_date: LocalDate, to_date: LocalDate) extends APIEntry

//Calendar entries have no id, so they can't extend APIEntry
case class LiveStream(country: String, caster: String, url: Uri, viewers: Int)
case class CalendarMatch(datetime: Instant, name: String, format: String, left_team: Long, right_team: Long)
case class CalendarEntry(date: Instant, stage: Long, is_live: Boolean, streams: List[LiveStream], matches: List[CalendarMatch])
*/

}