package domain

sealed trait NewStoneReaction
sealed trait DiscardReaction
case class Discard(tilePos: Int) extends NewStoneReaction
case class DeclareConcealedKong(tilePos0: Int, tilePos1: Int, tilePos2: Int, tilePos3: Int) extends NewStoneReaction
case class DeclareSmallMeldedKong(combinationPos: Int) extends NewStoneReaction
case object DeclareMahjong extends NewStoneReaction with DiscardReaction

case class DeclarePung(tile0: Int, tile1: Int, discard: Int) extends DiscardReaction
case class DeclareChow(tile0: Int, tile1: Int, discard: Int) extends DiscardReaction
case class DeclareBigMeldedKong(tile0: Int, tile1: Int, tile2: Int) extends DiscardReaction
case object DoNothing extends DiscardReaction

trait PlayerStrategy {
  def handleNewStone(hand: TileCollection): NewStoneReaction
  def handleDiscard(tile: TileCollection): DiscardReaction
}
