package state

import model.Node

case class GameLoop(initialNode: Node) {

  private var node: Node = initialNode

  def run(): Node = {

    while (node.state != End) {
      println(s"State: ${node.state}")
      node = node.state.transition(node)
    }
    println("The game has ended")

    node
  }

}
