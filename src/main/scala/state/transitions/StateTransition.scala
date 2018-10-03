package state.transitions

import domain.Table
import state.GameState

trait StateTransition[A <: GameState] {

  def next(current: A, table: Table): GameState

}
