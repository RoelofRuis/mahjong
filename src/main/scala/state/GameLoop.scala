package state

import domain.Table

class GameLoop(initialTable: Table, initialState: GameState, transition: (Table, GameState) => GameState) {

  private val table = initialTable
  private var currentState = initialState

  def run(): Unit = {
    while (currentState != End) {
      println(s"State: $currentState")
      currentState = transition(table, currentState)
    }
    println("The game has ended")
  }

}
