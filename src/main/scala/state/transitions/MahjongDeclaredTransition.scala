package state.transitions

import domain.Table
import state.{GameState, MahjongDeclared, NewRound}

class MahjongDeclaredTransition() extends StateTransition[MahjongDeclared] {

  def next(current: MahjongDeclared, table: Table): GameState = {
    // deal tile to player from dead wall

    NewRound()
  }

}
