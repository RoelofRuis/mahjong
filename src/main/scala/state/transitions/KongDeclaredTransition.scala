package state.transitions

import domain.{Dealer, Table}
import state.{GameState, KongDeclared, TileReceived}

class KongDeclaredTransition(dealer: Dealer) extends StateTransition[KongDeclared] {

  def next(current: KongDeclared, table: Table): GameState = {
    // deal tile to player from dead wall

    TileReceived()
  }

}
