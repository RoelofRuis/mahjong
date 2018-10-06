package domain

class Player(name: String) extends HandChecks {
  private var hand: Hand = Hand()

  protected def getHand: Hand = hand

  def receive(tiles: TileCollection): Unit = hand = hand.copy(concealedTiles = tiles.toArray)

  def describe(): String = s"Player $name holds: $hand"

  def reactToNewStone(): NewStoneReaction = Discard(0)

  def reactToDiscard(tile: Tile): DiscardReaction = DoNothing
}
