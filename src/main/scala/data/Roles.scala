package masterleague4s
package data

object Roles {

  sealed trait Role {
    def name: String
  }
  case object Warrior extends Role {
    val name = "Warrior"
  }
  case object Support extends Role {
    val name = "Support"
  }
  case object Assassin extends Role {
    val name = "Assassin"
  }
  case object Specialist extends Role {
    val name = "Specialist"
  }

  val roles = scala.collection.immutable.Map(
    1 -> Warrior,
    2 -> Support,
    3 -> Assassin,
    4 -> Specialist
  )

  val reverseRoles = Map[Role, Long](
    Warrior -> 1,
    Support -> 2,
    Assassin -> 3,
    Specialist -> 4

  )
}