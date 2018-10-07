package model

import scala.collection.immutable.Vector

sealed trait Wind
case object East extends Wind
case object South extends Wind
case object West extends Wind
case object North extends Wind

object Wind {

  val ORDER: Vector[Wind] = Vector[Wind](East, South, West, North)

}
