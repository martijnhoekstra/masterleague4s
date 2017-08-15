package masterleague4s
package data

case class PickF[+A, +B](hero: A, player: B)

object PickF {
  //todo: Implement bitraverse. Can this be done from tuple2?
}