package model

import scala.collection.immutable.Vector

case class Hand(
  concealedTiles: Vector[Tile] = Vector[Tile](),
  exposedCombinations: Vector[Combination] = Vector[Combination](),
  concealedCombinations: Vector[Combination] = Vector[Combination]()
)
