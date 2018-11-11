package model

object Tiles {

  sealed trait WindDirection
  case object North extends WindDirection
  case object South extends WindDirection
  case object East extends WindDirection
  case object West extends WindDirection

  sealed trait DragonColor
  case object Red extends DragonColor
  case object White extends DragonColor
  case object Green extends DragonColor

  sealed trait Suite
  case object Bamboos extends Suite
  case object Circles extends Suite
  case object Characters extends Suite

  case class TileNumber(int: Int) extends AnyVal

  sealed trait Tile
  case class DragonTile(color: DragonColor) extends Tile
  case class WindTile(windDirection: WindDirection) extends Tile
  case class SuitedTile(suite: Suite, number: TileNumber)

}
