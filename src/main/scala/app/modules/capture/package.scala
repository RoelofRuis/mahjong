package app.modules

import app.modules.game.model.Mahjong.Tile
import app.modules.game.model.Text._
import org.scalajs.dom
import org.scalajs.dom.ImageData
import upickle.default.{read, write}

import scala.util.Try

package object capture {

  type CapturedTiles = Map[(Tile, Int), Option[ImageData]]
  type StoredCapture = (String, (Int, Int, String))

  private val CAPTURE_STORAGE_KEY = "CAPTURED_TILES"

  def save(tiles: CapturedTiles): Unit = {
    val saveableTiles = tiles
      .collect { case ((tile, _), Some(image)) => (tile, image) }
      .toSeq
      .map[StoredCapture] { case (tile, i) =>
        val data = i.data
        val builder = new StringBuilder()
        for (b <- 0 until data.length / 4) {
          builder.addOne(data(b * 4).toChar)
        }
        val base64String = dom.window.btoa(builder.result())
        (tile.asText, (i.width, i.height, base64String))
      }
    dom.window.localStorage.setItem(CAPTURE_STORAGE_KEY, write(saveableTiles))
  }

  def load(): Option[Seq[StoredCapture]] = {
    for {
      data <- Option(dom.window.localStorage.getItem(CAPTURE_STORAGE_KEY))
      result <- Try(read[Seq[StoredCapture]](data)).toOption
    } yield result
  }

}
