package domain

class WallBuilder(tiles: TileCollection) {
  def newWall(): Wall = new Wall(tiles.shuffled())
}