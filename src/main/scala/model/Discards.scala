package model

import scala.collection.immutable.Vector

case class Discards(
  discards: Vector[Tile] = Vector[Tile]()
)
