package state.transitions

import domain.{Dealer, Table}
import state.{GameState, NewRound, NextTurn}

class NewRoundTransition(dealer: Dealer) extends StateTransition[NewRound] {

  def next(current: NewRound, table: Table): GameState = {
    table.getAllPlayers.foreach(dealer.dealStartingHand)

    NextTurn()
  }

}
