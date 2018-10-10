package model

import scalaz.{Lens, LensFamily}

import scala.collection.immutable.Vector

case class Wall(
  living: Vector[Tile],
  dead: Vector[Tile]
)

object Wall {

  val living: Lens[Wall, Vector[Tile]] = LensFamily.lensu[Wall, Vector[Tile]](
    (wall, tiles) => wall.copy(living = tiles),
    _.living
  )

  val dead: Lens[Wall, Vector[Tile]] = LensFamily.lensu[Wall, Vector[Tile]](
    (wall, tiles) => wall.copy(dead = tiles),
    _.dead
  )

}
