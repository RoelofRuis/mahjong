package model

import scalaz.{Lens, LensFamily}
import state.GameState

import scala.collection.immutable.Map

case class Node(
  state: GameState,
  windOfRound: Wind,
  wall: Wall,
  players: Map[Int, Player],
  activePlayer: Int
)

object Node {

  val wall: Lens[Node, Wall] = LensFamily.lensu[Node, Wall](
    (node, wall) => node.copy(wall = wall),
    _.wall
  )

  val players: Lens[Node, Map[Int, Player]] = LensFamily.lensu[Node, Map[Int, Player]](
    (node, players) => node.copy(players = players),
    _.players
  )

}
