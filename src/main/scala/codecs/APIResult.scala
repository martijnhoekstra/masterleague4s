package masterleagueapi
package codec

import spinoco.protocol.http.Uri

case class APIResult[A <: APIEntry](count: Int, next: Option[Uri], previous: Option[Uri], results: List[A])