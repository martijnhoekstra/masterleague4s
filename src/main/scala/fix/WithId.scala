package masterleague4s
package data

import simulacrum._

@typeclass trait WithId[A] {
  def id(a: A): Long
}