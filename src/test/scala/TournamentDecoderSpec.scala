package masterleagueapi
package codec

import io.circe.parser.decode
import org.specs2._
import Decoders._

class TournamentDecoderSpec extends Specification {
  def is = s2"""

  The Tournament decoder should
    parse id 35               $parse35
"""

  def id35string = """
        {
            "id": 35,
            "name": "HGC Europe - Open Division",
            "start_date": "2017-01-16",
            "end_date": "2017-10-15",
            "region": 1,
            "url": "https://masterleague.net/tournament/35/",
            "stages": [
                {
                    "id": 208,
                    "name": "Cup 9"
                },
                {
                    "id": 203,
                    "name": "Cup 8"
                },
                {
                    "id": 192,
                    "name": "Phase 1 Playoffs"
                },
                {
                    "id": 187,
                    "name": "Cup 7"
                },
                {
                    "id": 181,
                    "name": "Cup 6"
                },
                {
                    "id": 177,
                    "name": "Cup 5"
                },
                {
                    "id": 174,
                    "name": "Cup 4"
                },
                {
                    "id": 171,
                    "name": "Cup 3"
                },
                {
                    "id": 169,
                    "name": "Cup 2"
                },
                {
                    "id": 165,
                    "name": "Cup 1"
                }
            ]
        }"""

  def parse35 = {

    decode[TournamentEntry](id35string).isRight must beTrue
  }

}
