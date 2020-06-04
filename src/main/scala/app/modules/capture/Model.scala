package app.modules.capture

import app.modules.game.model.Mahjong.Tile
import org.scalajs.dom.raw.ImageData

object Model {

  final case class DetectionModel(
    tiles: Map[Tile, ImageData] = Map()
  )

}
