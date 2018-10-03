package state.transitions

import domain.{Dealer, Table}
import state.{GameState, NextTurn, TileReceived}

class NextTurnTransition(dealer: Dealer) extends StateTransition[NextTurn] {

  def next(current: NextTurn, table: Table): GameState = {
    val activePlayer = table.getActivePlayer

    dealer.deal(activePlayer)

    TileReceived()
  }

}