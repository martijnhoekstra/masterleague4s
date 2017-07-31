package masterleague4s
package codec

import io.circe.parser.decode
import org.specs2._
import masterleague4s.data._
import Serialized._
import FDecoders._

class HeroDecoderSpec extends Specification {
  def is = s2"""

  The Hero decoder should
    parse Abathur               $parseAbathur
"""

  def abathurString = """{
    "id": 37,
    "name": "Abathur",
    "role": 4,
    "url": "https://masterleague.net/hero/abathur/",
    "portrait": {
      "small": "https://c.masterleague.net/media/hero/portrait/1458393521.jpg.50x25_q85_crop-0%2C-140.jpg",
      "medium": "https://c.masterleague.net/media/hero/portrait/1458393521.jpg.90x135_q85_crop.jpg"
    }
  }"""

  def parseAbathur = {

    decode[IdHero](abathurString).isRight must beTrue
  }

}

/*
case class HeroPortrait(small: Uri, medium: Uri)
case class HeroEntry(id: Long, name: String, role: Long, url: Uri, portrait: HeroPortrait) extends APIEntry
case class TeamLogo(small: Uri, medium: Uri, large: Uri)
case class TeamEntry(id: Long, name: String, region: Long, url: Uri, logo: TeamLogo) extends APIEntry
case class PlayerPhoto(small: Uri, big: Uri, medium: Uri)
case class PlayerEntry(id: Long, team: Option[Long], region: Long, nickname: String, realname: String, country: String, role: Long, url: Uri, photo: PlayerPhoto) extends APIEntry
case class TournamentStage(id: Long, name: String) //looks like id is the id of a TournamentEntry? Also related to Match: Crawl and check
case class TournamentEntry(id: Long, name: String, start_date: LocalDate, end_date: LocalDate, region: Long, url: Uri, stages: List[TournamentStage]) extends APIEntry
case class Pick(hero: Long, player: Long)
case class Draft(team: Long, is_winner: Boolean, bans: List[Long], picks: List[Pick])
case class MatchEntry(id: Long, date: Instant, patch: Long, tournament: Long, stage: Long, round: String, series: Long, game: Int, map: Long, url: Uri, drafts: List[Draft]) extends APIEntry {
  def winner = drafts.find(_.is_winner).get
  def loser = drafts.find(d => !d.is_winner).get
  def firstPick = drafts(0)
  def secondPick = drafts(1)
  def firstPickWins = drafts(0) == winner
}

sealed trait PlainEntry {
  def id: Long
}
//the following endpoint doesn't follow the usual pattern, but are a plain array
case class MapEntry(id: Long, name: String, url: Uri) extends PlainEntry
case class RegionEntry(id: Long, name: String) extends PlainEntry
case class PatchEntry(id: Long, name: String, from_date: LocalDate, to_date: LocalDate) extends PlainEntry //"2017-05-15"

*/ 