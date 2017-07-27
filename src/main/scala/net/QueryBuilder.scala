package masterleagueapi
package net

import spinoco.protocol.http.Uri.Query
import simulacrum._

@typeclass trait QueryBuilder[A] {
  def query(a: A): Query
}