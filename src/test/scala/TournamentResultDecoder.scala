package masterleague4s
package codec

import io.circe.parser.decode
import org.specs2._
import masterleague4s.data._
import IdAnnotated._
import FDecoders._
class TournamentResultDecoderSpec extends Specification {
  def is = s2"""

  The TournamentResult decoder should
    parse page 1              $parse35
"""

  def page1string = """{
"count": 42,
"next": "https://api.masterleague.net/tournaments/?format=json&page=2",
"previous": null,
"results": [
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
},
{
"id": 36,
"name": "HGC North America - Open Division",
"start_date": "2017-01-24",
"end_date": "2017-10-15",
"region": 2,
"url": "https://masterleague.net/tournament/36/",
"stages": [
{
"id": 210,
"name": "Cup 9"
},
{
"id": 204,
"name": "Cup 8"
},
{
"id": 193,
"name": "Phase 1 Playoffs"
},
{
"id": 191,
"name": "Cup 7"
},
{
"id": 182,
"name": "Cup 6"
},
{
"id": 179,
"name": "Cup 5"
},
{
"id": 176,
"name": "Cup 4"
},
{
"id": 173,
"name": "Cup 3"
},
{
"id": 170,
"name": "Cup 2"
},
{
"id": 166,
"name": "Cup 1"
}
]
},
{
"id": 43,
"name": "HGC Europe - Phase 2",
"start_date": "2017-06-23",
"end_date": "2017-10-08",
"region": 1,
"url": "https://masterleague.net/tournament/43/",
"stages": [
{
"id": 205,
"name": "Group stage 1"
}
]
},
{
"id": 44,
"name": "HGC North America - Phase 2",
"start_date": "2017-06-23",
"end_date": "2017-10-08",
"region": 2,
"url": "https://masterleague.net/tournament/44/",
"stages": [
{
"id": 206,
"name": "Group stage 1"
}
]
},
{
"id": 45,
"name": "HGC Korea - Phase 2",
"start_date": "2017-06-23",
"end_date": "2017-10-08",
"region": 3,
"url": "https://masterleague.net/tournament/45/",
"stages": [
{
"id": 207,
"name": "Group stage 1"
}
]
},
{
"id": 46,
"name": "HGC China - Phase 2",
"start_date": "2017-06-26",
"end_date": "2017-10-08",
"region": 4,
"url": "https://masterleague.net/tournament/46/",
"stages": [
{
"id": 209,
"name": "Group stage 1"
}
]
},
{
"id": 42,
"name": "Mid-Season Brawl",
"start_date": "2017-06-10",
"end_date": "2017-06-19",
"region": null,
"url": "https://masterleague.net/tournament/42/",
"stages": [
{
"id": 202,
"name": "Playoffs"
},
{
"id": 201,
"name": "Group B"
},
{
"id": 200,
"name": "Group A"
}
]
},
{
"id": 32,
"name": "HGC North America - Phase 1",
"start_date": "2016-11-21",
"end_date": "2017-05-28",
"region": 2,
"url": "https://masterleague.net/tournament/32/",
"stages": [
{
"id": 198,
"name": "HGC Crucible"
},
{
"id": 196,
"name": "Playoffs"
},
{
"id": 190,
"name": "Standings"
},
{
"id": 185,
"name": "Group stage 2"
},
{
"id": 168,
"name": "Group stage 1"
},
{
"id": 147,
"name": "Qualifier #3"
},
{
"id": 146,
"name": "Qualifier #2"
},
{
"id": 145,
"name": "Qualifier #1"
}
]
},
{
"id": 33,
"name": "HGC Europe - Phase 1",
"start_date": "2016-11-19",
"end_date": "2017-05-28",
"region": 1,
"url": "https://masterleague.net/tournament/33/",
"stages": [
{
"id": 197,
"name": "HGC Crucible"
},
{
"id": 194,
"name": "Playoffs"
},
{
"id": 189,
"name": "Standings"
},
{
"id": 184,
"name": "Group stage 2"
},
{
"id": 167,
"name": "Group stage 1"
},
{
"id": 144,
"name": "Qualifier #3"
},
{
"id": 143,
"name": "Qualifier #2"
},
{
"id": 142,
"name": "Qualifier #1"
}
]
},
{
"id": 37,
"name": "HGC Korea - Phase 1",
"start_date": "2017-02-03",
"end_date": "2017-05-28",
"region": 3,
"url": "https://masterleague.net/tournament/37/",
"stages": [
{
"id": 199,
"name": "HGC Crucible"
},
{
"id": 195,
"name": "Playoffs"
},
{
"id": 188,
"name": "Standings"
},
{
"id": 186,
"name": "Group stage 2"
},
{
"id": 172,
"name": "Group stage 1"
}
]
},
{
"id": 40,
"name": "HGC China - Phase 1",
"start_date": "2017-04-03",
"end_date": "2017-05-28",
"region": 4,
"url": "https://masterleague.net/tournament/40/",
"stages": [
{
"id": 180,
"name": "Group stage"
}
]
},
{
"id": 41,
"name": "King of the Storm",
"start_date": "2017-03-15",
"end_date": "2017-04-09",
"region": 1,
"url": "https://masterleague.net/tournament/41/",
"stages": [
{
"id": 183,
"name": "Playoff"
}
]
},
{
"id": 39,
"name": "Eastern Clash",
"start_date": "2017-03-17",
"end_date": "2017-03-19",
"region": null,
"url": "https://masterleague.net/tournament/39/",
"stages": [
{
"id": 178,
"name": "Playoff"
}
]
},
{
"id": 38,
"name": "Western Clash",
"start_date": "2017-03-03",
"end_date": "2017-03-05",
"region": null,
"url": "https://masterleague.net/tournament/38/",
"stages": [
{
"id": 175,
"name": "Playoff"
}
]
},
{
"id": 34,
"name": "Gold Series 2016 - Grand Finals",
"start_date": "2016-12-16",
"end_date": "2017-01-08",
"region": 4,
"url": "https://masterleague.net/tournament/34/",
"stages": [
{
"id": 163,
"name": "Playoffs"
},
{
"id": 164,
"name": "Tiebreak"
},
{
"id": 161,
"name": "Group stage"
}
]
},
{
"id": 19,
"name": "ZOTAC Cup",
"start_date": "2015-03-07",
"end_date": "2016-12-17",
"region": 1,
"url": "https://masterleague.net/tournament/19/",
"stages": [
{
"id": 159,
"name": "Monthly #21"
},
{
"id": 158,
"name": "Weekly #70"
},
{
"id": 157,
"name": "Weekly #69"
},
{
"id": 154,
"name": "Monthly #20"
},
{
"id": 150,
"name": "Weekly #68"
},
{
"id": 152,
"name": "Monthly #19"
},
{
"id": 149,
"name": "Weekly #67"
},
{
"id": 148,
"name": "Weekly #66"
},
{
"id": 141,
"name": "Weekly #65"
},
{
"id": 140,
"name": "Weekly #64"
},
{
"id": 139,
"name": "Weekly #63"
},
{
"id": 138,
"name": "Weekly #62"
},
{
"id": 135,
"name": "Monthly #18"
},
{
"id": 127,
"name": "Weekly #61"
},
{
"id": 124,
"name": "Weekly #60"
},
{
"id": 121,
"name": "Weekly #59"
},
{
"id": 117,
"name": "Monthly #17"
},
{
"id": 115,
"name": "Weekly #58"
},
{
"id": 105,
"name": "Weekly #57"
},
{
"id": 104,
"name": "Weekly #56"
},
{
"id": 100,
"name": "Monthly #16"
},
{
"id": 93,
"name": "Weekly #55"
},
{
"id": 91,
"name": "Weekly #54"
},
{
"id": 89,
"name": "Weekly #53"
},
{
"id": 88,
"name": "Weekly #52"
},
{
"id": 78,
"name": "Monthly #15"
},
{
"id": 92,
"name": "Weekly #51"
},
{
"id": 77,
"name": "Monthly #14"
}
]
},
{
"id": 31,
"name": "Gold Club World Championship",
"start_date": "2016-11-11",
"end_date": "2016-12-04",
"region": null,
"url": "https://masterleague.net/tournament/31/",
"stages": [
{
"id": 156,
"name": "Playoffs"
},
{
"id": 155,
"name": "Group Stage"
},
{
"id": 153,
"name": "Chinese Wildcard"
},
{
"id": 151,
"name": "Global Wildcard"
}
]
},
{
"id": 30,
"name": "Fall Global Championship",
"start_date": "2016-10-26",
"end_date": "2016-11-05",
"region": null,
"url": "https://masterleague.net/tournament/30/",
"stages": [
{
"id": 134,
"name": "Playoffs"
},
{
"id": 133,
"name": "Group Stage #2 - Group B"
},
{
"id": 132,
"name": "Group Stage #2 - Group A"
},
{
"id": 131,
"name": "Group Stage #1 - Group B"
},
{
"id": 130,
"name": "Group Stage #1 - Group A"
}
]
},
{
"id": 28,
"name": "North America Nexus Games",
"start_date": "2016-09-23",
"end_date": "2016-10-16",
"region": 2,
"url": "https://masterleague.net/tournament/28/",
"stages": [
{
"id": 137,
"name": "Playoffs"
},
{
"id": 123,
"name": "Qualifier"
}
]
},
{
"id": 27,
"name": "Europe Nexus Games",
"start_date": "2016-09-09",
"end_date": "2016-10-09",
"region": 1,
"url": "https://masterleague.net/tournament/27/",
"stages": [
{
"id": 136,
"name": "Playoffs"
},
{
"id": 122,
"name": "Qualifier (Top 16)"
}
]
}
]
}"""

  def parse35 = {
    val parseResult = decode[APIResult[TournamentId]](page1string)
    parseResult.isRight must beTrue
  }

}