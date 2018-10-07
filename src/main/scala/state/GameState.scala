package state

import model.Node

trait GameState {

  def transition(node: Node): Node

}

case object End extends GameState {

  def transition(node: Node): Node = throw new RuntimeException("The game has ended")

}