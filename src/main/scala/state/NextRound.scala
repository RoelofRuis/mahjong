package state

import model._
import scala.collection.immutable.Vector

object NextRound extends GameState {

  override def transition(node: Node): Node = {
    val livingWall = Node.wall >=> Wall.living
    def playerConcealedTiles(i: Int) = Node.players.at(i) >=> Player.hand >=> Hand.concealedTiles

    var varNode = node

    for (i: Int <- 0 until node.players.size) {
      val (taken: Vector[Tile], left: Vector[Tile]) = livingWall.get(varNode).splitAt(13)
      varNode = playerConcealedTiles(i).set(varNode, taken)
      varNode = livingWall.set(varNode, left)
    }

    varNode.copy(state = End)
  }
}
