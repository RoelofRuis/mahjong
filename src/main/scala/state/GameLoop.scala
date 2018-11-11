package state

import model.Table

object GameLoop {

  def run(initialState: Table, transition: Transition): Table = {

    val (nextState, nextTransition) = transition.run(initialState)

    if (nextTransition.isDefined) run(nextState, nextTransition.get)
    else nextState
  }

}
