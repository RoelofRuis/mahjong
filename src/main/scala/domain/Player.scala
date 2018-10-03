package domain

class Player(name: String) {
  private val hand: Hand = new TileCollection()

  def receive(tiles: TileCollection): Unit = hand.insert(tiles)

  def describe(): String = s"Player $name holds: ${hand.describe()}"

  def reactToNewStone(): NewStoneReaction = DeclareMahjong(hand)

  def reactToDiscard(tile: Tile): DiscardReaction = DoNothing
}
