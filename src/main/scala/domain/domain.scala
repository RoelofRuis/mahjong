package object domain {

  type Tile = String

  trait Combination

  case class Pung(tile: Tile) extends Combination
  case class Kong(tile: Tile) extends Combination
  case class Chow(tile: Tile) extends Combination

}