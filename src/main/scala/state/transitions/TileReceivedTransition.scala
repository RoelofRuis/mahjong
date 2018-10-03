package state.transitions

import domain._
import state._

class TileReceivedTransition extends StateTransition[TileReceived] {

  def next(current: TileReceived, table: Table): GameState = {
    val activePlayer = table.getActivePlayer

    activePlayer.reactToNewStone() match {
      case Discard(tile) => TileDiscarded(tile)
      case DeclareHiddenKong => HiddenKongDeclared()
      case CompleteOpenPung => OpenPungCompleted()
      case DeclareMahjong(hand) => MahjongDeclared()
    }
  }

}