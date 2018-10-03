package domain

class Dealer(wallBuilder: WallBuilder) {
  private val wall: Wall = wallBuilder.newWall()

  def dealStartingHand(player: Player): Unit = player.receive(wall.take(13))

  def deal(player: Player): Unit = player.receive(wall.take(1))
}
