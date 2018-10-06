package domain

case class Hand(
                 concealedTiles: Array[Tile] = Array(),
                 exposedCombinations: Array[Combination] = Array(),
                 concealedCombinations: Array[Combination] = Array()
) {
  def getPlayableTiles: Map[Int, Tile] = mapWithIndex(concealedTiles)

  def getExposedCombinations: Map[Int, Combination] = mapWithIndex(exposedCombinations)

  def getConcealedCombinations: Map[Int, Combination] = mapWithIndex(concealedCombinations)

  private def mapWithIndex[A](data: Array[A]): Map[Int, A] = {
    data.zipWithIndex.map { case (data: A, index) => (index, data) }.toMap
  }
}
