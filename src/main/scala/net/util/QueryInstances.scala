package masterleague4s
package net
package util

import spinoco.protocol.http.Uri.Query
import cats._

object QueryInstances {
  trait Instances extends Monoid[Query] with Show[Query] {
    def empty: Query = Query.empty
    def combine(x: Query, y: Query): Query = {
      val params = (x.params ++ y.params).groupBy(_._1).toList.collect { case (_, (k, v) :: _) => (k, v) }
      Query(params)
    }
    def show(q: Query) = q.params.map(pair => pair match {
      case (k, "") => k
      case (k, v) => s"$k=$v"
    }).mkString("?", "&", "")
  }

  implicit val instances: Instances = new Instances {}
}