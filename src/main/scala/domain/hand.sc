import domain.Tile

trait Combination

case class Pung(tile: Tile) extends Combination
case class Kong(tile: Tile) extends Combination
case class Chow(tile: Tile) extends Combination

case class Hand(
  closedTiles: Array[Tile] = Array(),
  openCombinations: Array[Combination] = Array(),
  closedCombinations: Array[Combination] = Array()
) {

  def getPlayableTiles: Map[Int, Tile] = {
    closedTiles.zipWithIndex.map { case (tile, index) => (index, tile) }.toMap
  }

  def getOpenCombinations: Array[Combination] = openCombinations

  def getClosedCombinations: Array[Combination] = closedCombinations

}

val hand = Hand(Array("blaat", "grok", "replika"))

println(hand.getPlayableTiles)