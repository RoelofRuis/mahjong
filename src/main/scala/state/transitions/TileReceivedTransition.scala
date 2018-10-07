package state.transitions

// TODO: Move to state object
//class TileReceivedTransition extends StateTransition[TileReceived] {
//
//  def next(current: TileReceived, table: Table): GameState = {
//    val activePlayer = table.getActivePlayer
//
//    activePlayer.reactToNewStone() match {
//      case Discard(tilePos: Int) =>
//        val tile = activePlayer.tileByPos(tilePos).get // TODO: add check if pos is valid...
//        TileDiscarded(tile)
//
//      case DeclareConcealedKong(tilePos0, tilePos1, tilePos2, tilePos3) =>
//        val valid = activePlayer.validConcealedKong(tilePos0, tilePos1, tilePos2, tilePos3)
//        if (valid) KongDeclared() // TODO: add transition logic
//        else NextTurn() // TODO: add dead game logic
//
//      case DeclareSmallMeldedKong(combinationPos, tilePos) =>
//        val valid = activePlayer.validSmallMeldedKong(combinationPos, tilePos)
//        if (valid) KongDeclared() // TODO: add transition logic
//        else NextTurn() // // TODO: add dead game logic
//
//      case DeclareMahjong =>
//        if (activePlayer.validMahjong()) MahjongDeclared()
//        else NextTurn() // TODO: add dead game logic
//    }
//  }
//
//}