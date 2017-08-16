package masterleague4s
package net

import org.specs2._

class EndpointsSpec extends Specification {

  def is = s2"""
  host is secure             $shost
  auth is secure             $sauth
  matches is secure          $smatches
  battlegrounds is secure    $sbattlegrounds
  regions is secure          $sregions
  teams is secure            $steams
  patches is secure          $spatches
  players is secure          $splayers
  tournaments is secure      $stournaments
  calendar is secure         $scalendar
"""

  import Endpoints._
  import spinoco.protocol.http.HttpScheme._

  def shost = host.scheme === HTTPS
  def sauth = auth.scheme === HTTPS
  def smatches = matches.scheme === HTTPS
  def sbattlegrounds = battlegrounds.scheme === HTTPS
  def sregions = regions.scheme === HTTPS
  def steams = teams.scheme === HTTPS
  def spatches = patches.scheme === HTTPS
  def splayers = players.scheme === HTTPS
  def stournaments = tournaments.scheme === HTTPS
  def scalendar = calendar.scheme === HTTPS

}