package masterleague4s
package instances
package net
package util

import cats.kernel.laws.GroupLaws
import spinoco.protocol.http.Uri.Query
import masterleague4s.instances.Generators._
import masterleague4s.net.util.QueryInstances._

class QueryInstancesSpec extends DisciplineSpec {
  def is = s2"""
  Query forms a monoid       $e1
  """

  def e1 = checkAll("Query", GroupLaws[Query].monoid)
}
