package domain

sealed trait NewStoneReaction
sealed trait DiscardReaction
case class Discard(tile: Tile) extends NewStoneReaction
case object DeclareHiddenKong extends NewStoneReaction
case object CompleteOpenPung extends NewStoneReaction
case class DeclareMahjong(hand: Hand) extends NewStoneReaction with DiscardReaction
case object DeclarePung extends DiscardReaction
case object DeclareKong extends DiscardReaction
case object DeclareChow extends DiscardReaction
case object DoNothing extends DiscardReaction

trait PlayerStrategy {
  def handleNewStone(hand: TileCollection): NewStoneReaction
  def handleDiscard(tile: TileCollection): DiscardReaction
}
