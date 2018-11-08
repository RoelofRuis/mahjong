package model

import scala.collection.immutable.Vector

case class Wall(
  living: Vector[Tile],
  dead: Vector[Tile]
) {

  def takeFromLiving(n: Int): (Vector[Tile], Wall) = {
    val newWall = Wall(
      living.drop(n),
      dead,
    )
    (living.take(n), newWall)
  }

}
