package masterleague4s
package net
package util

import spinoco.protocol.http.Uri.Query
import cats._

object QueryInstances {
  trait Instances extends Monoid[Query] with Show[Query] with Eq[Query] {
    def empty: Query = Query.empty
    def combine(x: Query, y: Query): Query = {
      //only lawful for alphabetically ordered params by key with no duplicates
      //this is not spec-enforced nor generally valid
      //but it is for the use-case
      val params = ((y.params ::: x.params).groupBy(_._1).toList.collect { case (_, (k, v) :: _) => (k, v) }).sortBy(_._1)
      Query(params)
    }
    def show(q: Query) = q.params.map(pair => pair match {
      case (k, "") => k
      case (k, v) => s"$k=$v"
    }).mkString("?", "&", "")

    def eqv(x: Query, y: Query) = x == y

  }

  implicit val instances: Instances = new Instances {}
}