package masterleague4s
package data

import spinoco.protocol.http.Uri
case class LiveStream(country: String, caster: String, url: Uri, viewers: Int)