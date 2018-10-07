package state

import model.{Hand, Node}

object NextRound extends GameState {

  override def transition(node: Node): Node = {
    val tiles = node.wall.living.take(13)

    node.copy(
      players = node.players.updated(
        node.activePlayer,
        node.players(node.activePlayer).copy(
          hand = Hand(tiles)
        )
      ),
      wall = node.wall.copy(living = node.wall.living.drop(13)),
      state = End
    )
  }
}
