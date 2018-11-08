package state

import model._

import scala.collection.immutable.Vector

object NextRound {

  def transition: Transition = Transition { node =>
    def dealInitialHands(node: Node, players: Iterable[Wind]): Node = {
      if (players.isEmpty) node
      else {
        val (taken: Vector[Tile], wall: Wall) = node.wall.takeFromLiving(14)
        val node1 = node.addPlayerConcealedTiles(players.head, taken)
        dealInitialHands(node1.setWall(wall), players.tail)
      }
    }

    (dealInitialHands(node, node.players.keys), None)
  }
}