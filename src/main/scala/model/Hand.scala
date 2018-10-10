package model

import scalaz.{Lens, LensFamily}

import scala.collection.immutable.Vector

case class Hand(
  concealedTiles: Vector[Tile] = Vector[Tile](),
  exposedCombinations: Vector[Combination] = Vector[Combination](),
  concealedCombinations: Vector[Combination] = Vector[Combination]()
)

object Hand {

  val concealedTiles: Lens[Hand, Vector[Tile]] = LensFamily.lensu[Hand, Vector[Tile]](
    (hand, tiles) => hand.copy(concealedTiles = tiles),
    _.concealedTiles
  )

}