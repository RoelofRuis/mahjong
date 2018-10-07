package model

import scala.collection.immutable.Vector

case class Wall(
  living: Vector[Tile],
  dead: Vector[Tile]
)
