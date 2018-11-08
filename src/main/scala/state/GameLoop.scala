package state

import model.Node

object GameLoop {

  def run(initialState: Node, transition: Transition): Node = {

    val (nextState, nextTransition) = transition.run(initialState)

    if (nextTransition.isDefined) run(nextState, nextTransition.get)
    else nextState
  }

}
