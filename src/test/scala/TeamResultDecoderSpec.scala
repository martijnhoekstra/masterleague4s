package masterleague4s
package codec

import io.circe.parser.decode
import org.specs2._
import masterleague4s.data._
import Serialized._
import FDecoders._
class TeamResultDecoderSpec extends Specification {
  def is = s2"""

  The Team decoder should
    parse page 1              $parsesuccess
"""

  val page1string = """{
    "count": 180,
    "next": "https://api.masterleague.net/teams/?page=2",
    "previous": null,
    "results": [
        {
            "id": 40,
            "name": "1.4",
            "region": 3,
            "url": "https://masterleague.net/team/1-4/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1458220619.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1458220619.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1458220619.png.150x140_q85.png"
            }
        },
        {
            "id": 73,
            "name": "2ARC Gaming",
            "region": 2,
            "url": "https://masterleague.net/team/2arc-gaming/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1461144018.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1461144018.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1461144018.png.150x140_q85.png"
            }
        },
        {
            "id": 165,
            "name": "4k MMR Average",
            "region": 1,
            "url": "https://masterleague.net/team/tossers/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/default.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/default.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/default.png.150x140_q85.png"
            }
        },
        {
            "id": 179,
            "name": "AE InSecure",
            "region": 1,
            "url": "https://masterleague.net/team/ae-insecure/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/default.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/default.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/default.png.150x140_q85.png"
            }
        },
        {
            "id": 79,
            "name": "Astral Authority",
            "region": 2,
            "url": "https://masterleague.net/team/astral-authority/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1462092032.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1462092032.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1462092032.png.150x140_q85.png"
            }
        },
        {
            "id": 152,
            "name": "B-Step",
            "region": 2,
            "url": "https://masterleague.net/team/b-step/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1484399223.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1484399223.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1484399223.png.150x140_q85.png"
            }
        },
        {
            "id": 139,
            "name": "Ballistix",
            "region": 3,
            "url": "https://masterleague.net/team/ballistix/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1476295646.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1476295646.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1476295646.png.150x140_q85.png"
            }
        },
        {
            "id": 76,
            "name": "BayWayStar",
            "region": 4,
            "url": "https://masterleague.net/team/baywaystar/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1461317626.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1461317626.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1461317626.png.150x140_q85.png"
            }
        },
        {
            "id": 136,
            "name": "beGenius ESC",
            "region": 1,
            "url": "https://masterleague.net/team/begenius-esc/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1484399102.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1484399102.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1484399102.png.150x140_q85.png"
            }
        },
        {
            "id": 53,
            "name": "Big Gods",
            "region": 8,
            "url": "https://masterleague.net/team/big-gods/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1459423251.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1459423251.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1459423251.png.150x140_q85.png"
            }
        },
        {
            "id": 167,
            "name": "BlossoM",
            "region": 3,
            "url": "https://masterleague.net/team/blossom/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1487505636.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1487505636.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1487505636.png.150x140_q85.png"
            }
        },
        {
            "id": 122,
            "name": "BooM",
            "region": 3,
            "url": "https://masterleague.net/team/boom/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/default.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/default.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/default.png.150x140_q85.png"
            }
        },
        {
            "id": 83,
            "name": "Brain Power",
            "region": 2,
            "url": "https://masterleague.net/team/brain-power/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/default.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/default.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/default.png.150x140_q85.png"
            }
        },
        {
            "id": 67,
            "name": "Brave Heart",
            "region": 4,
            "url": "https://masterleague.net/team/brave-heart/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1460840128.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1460840128.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1460840128.png.150x140_q85.png"
            }
        },
        {
            "id": 65,
            "name": "Brave Heart.Super",
            "region": 4,
            "url": "https://masterleague.net/team/brave-heart-super/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1460838981.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1460838981.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1460838981.png.150x140_q85.png"
            }
        },
        {
            "id": 138,
            "name": "Burning Rage",
            "region": 8,
            "url": "https://masterleague.net/team/burning-rage/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1476186770.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1476186770.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1476186770.png.150x140_q85.png"
            }
        },
        {
            "id": 190,
            "name": "Bushido e-Sports",
            "region": 1,
            "url": "https://masterleague.net/team/bushido-e-sports/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/default.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/default.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/default.png.150x140_q85.png"
            }
        },
        {
            "id": 62,
            "name": "By Far...",
            "region": 1,
            "url": "https://masterleague.net/team/by-far/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1460285670.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1460285670.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1460285670.png.150x140_q85.png"
            }
        },
        {
            "id": 177,
            "name": "Casual Dreamers",
            "region": 1,
            "url": "https://masterleague.net/team/casual-dreamers/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/default.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/default.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/default.png.150x140_q85.png"
            }
        },
        {
            "id": 68,
            "name": "Casual Logic Gaming",
            "region": 1,
            "url": "https://masterleague.net/team/casual-logic-gaming/",
            "logo": {
                "small": "https://c.masterleague.net/media/team/logo/1460892627.png.80x80_q85.png",
                "big": "https://c.masterleague.net/media/team/logo/1460892627.png.300x300_q85.png",
                "medium": "https://c.masterleague.net/media/team/logo/1460892627.png.150x140_q85.png"
            }
        }
    ]
}"""

  def parsesuccess = {
    val parseResult = decode[UriApiResult[IdTeam]](page1string)
    parseResult.isRight must beTrue
  }

}