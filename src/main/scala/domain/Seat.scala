package domain

class Seat(player: Player, initialPosition: Int) {
  private val position: Int = initialPosition

  def getPosition: Int = position

  def getPlayer: Player = player
}
