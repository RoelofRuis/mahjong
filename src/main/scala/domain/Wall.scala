package domain

class Wall(tiles: TileCollection) {
  private val deadWall = tiles.take(14)
  private val livingWall = tiles

  def take(n: Int): TileCollection = livingWall.take(n)

  def takeFromDeadWall(): TileCollection = {
    val result = deadWall.take(1)
    deadWall.insert(livingWall.take(1))
    result
  }

  def allowsTaking(n: Int): Boolean = leftForTaking >= n

  def leftForTaking(): Int = livingWall.size()

}
