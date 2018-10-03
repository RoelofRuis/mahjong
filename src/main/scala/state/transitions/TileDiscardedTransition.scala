package state.transitions

import domain._
import state._

class TileDiscardedTransition extends StateTransition[TileDiscarded] {

  def next(current: TileDiscarded, table: Table): GameState = {
    val orderedReactions: List[(DiscardReaction, Seat)] = table.getNonActiveSeats
      .map(getSeatReaction(_, current.tile))
      .toList
      .sortBy { case (value, seat, _, _) => (value, seat) }
      .map { case (_, _, reaction, player) => (reaction, player) }

    def winningReaction(l: List[(DiscardReaction, Seat)]): GameState = l match {
      case Nil => NextTurn()

      case (DoNothing, _) :: _ => NextTurn()

      case (DeclareChow, seat) :: tail =>
        if (validChow) ChowDeclared()
        else winningReaction(tail)

      case (DeclarePung, seat) :: tail =>
        if (validPung) PungDeclared()
        else winningReaction(tail)

      case (DeclareKong, seat: Seat) :: tail =>
        if (validKong) KongDeclared()
        else winningReaction(tail)

      case (mahjong: DeclareMahjong, seat: Seat) :: tail =>
        if (validMahjong) MahjongDeclared()
        else winningReaction(tail)
    }

    winningReaction(orderedReactions)
  }

  private def validChow(): Boolean = ???
  private def validPung(): Boolean = ???
  private def validKong(): Boolean = ???
  private def validMahjong(): Boolean = ???

  private def getSeatReaction(seat: Seat, tile: Tile): (Int, Int, DiscardReaction, Seat) = {
    val reaction = seat.getPlayer.reactToDiscard(tile)
    val value = reaction match {
      case DoNothing => 0
      case DeclareChow => 1
      case DeclarePung => 2
      case DeclareKong => 3
      case DeclareMahjong(_) => 4
    }
    (value, seat.getPosition, reaction, seat)
  }
}