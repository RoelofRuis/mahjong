package model

import model.Mahjong.{Bamboos, Characters, Circles, DragonTile, East, North, South, SuitedTile, Tile, West, WindDirection, WindTile}

object Text {

  implicit class TileText(tile: Tile) {
    def asText: String = {
      tile match {
        case SuitedTile(Bamboos, i) => s"$i of Bamboos"
        case SuitedTile(Circles, i) => s"$i of Circles"
        case SuitedTile(Characters, i) => s"$i of Characters"
        case WindTile(d) => s"$d Wind"
        case DragonTile(c) => s"$c Dragon"
      }
    }
  }

  implicit class WindDirectionText(windDirection: WindDirection) {
    def asChar: String = windDirection match {
      case West => "W"
      case East => "E"
      case North => "N"
      case South => "S"
    }
  }

}
