package state.transitions

// TODO: Move to state object
//class TileDiscardedTransition extends StateTransition[TileDiscarded] {
//
//  def next(current: TileDiscarded, table: Table): GameState = {
//    val orderedReactions: List[(DiscardReaction, Player)] = table.getNonActiveSeats
//      .map(getSeatReaction(_, current.tile))
//      .toList
//      .sortBy { case (value, seat, _, _) => (value, seat) }
//      .map { case (_, _, reaction, seat) => (reaction, seat.getPlayer) }
//
//    def winningReaction(l: List[(DiscardReaction, Player)]): GameState = l match {
//      case Nil => NextTurn()
//
//      case (DoNothing, _) :: _ => NextTurn()
//
//      case (DeclareChow(tilePos0, tilePos1, discard), player) :: tail =>
//        if (player.validChow(tilePos0, tilePos1, current.tile))
//          // TODO: check option & update hand
//          TileDiscarded(player.tileByPos(discard).get)
//        else winningReaction(tail)
//
//      case (DeclarePung(tilePos0, tilePos1, discard), player) :: tail =>
//        if (player.validPung(tilePos0, tilePos1, current.tile))
//          // TODO: check option & update hand
//          TileDiscarded(player.tileByPos(discard).get)
//        else winningReaction(tail)
//
//      case (DeclareBigMeldedKong(tilePos0, tilePos1, tilePos2), player) :: tail =>
//        if (player.validBigMeldedKong(tilePos0, tilePos1, tilePos2, current.tile))
//          // TODO: check option & update hand
//          KongDeclared()
//        else winningReaction(tail)
//
//      case (DeclareMahjong, player) :: tail =>
//        if (player.validMahjong()) MahjongDeclared()
//        else winningReaction(tail)
//    }
//
//    winningReaction(orderedReactions)
//  }
//
//  private def getSeatReaction(seat: Seat, tile: Tile): (Int, Int, DiscardReaction, Seat) = {
//    val reaction = seat.getPlayer.reactToDiscard(tile)
//    val value = reaction match {
//      case DoNothing => 0
//      case _: DeclareChow => 1
//      case _: DeclarePung => 2
//      case _: DeclareBigMeldedKong => 3
//      case DeclareMahjong => 4
//    }
//    (value, seat.getPosition, reaction, seat)
//  }
//}