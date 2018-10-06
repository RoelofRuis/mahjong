package state.transitions

import domain._
import state._

class TileReceivedTransition extends StateTransition[TileReceived] {

  def next(current: TileReceived, table: Table): GameState = {
    val activePlayer = table.getActivePlayer

    activePlayer.reactToNewStone() match {
      case Discard(tilePos: Int) =>
        val tile = activePlayer.tileByPos(tilePos).get // TODO: add check if pos is valid...
        TileDiscarded(tile)

      case DeclareConcealedKong(tilePos0, tilePos1, tilePos2, tilePos3) =>
        val valid = activePlayer.validConcealedKong(tilePos0, tilePos1, tilePos2, tilePos3)
        if (valid) KongDeclared() // TODO: add transition logic
        else NextTurn() // TODO: add dead game logic

      case _: DeclareSmallMeldedKong =>
        // TODO: add transition logic
        KongDeclared()

      case DeclareMahjong =>
        if (activePlayer.validMahjong()) MahjongDeclared()
        else NextTurn() // TODO: add dead game logic
    }
  }

}