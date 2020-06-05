package app.modules.capture

import app.HTML
import app.modules.game.model.Mahjong.Tile
import org.scalajs.dom.experimental.mediastream.{MediaDevices, MediaStreamConstraints}
import org.scalajs.dom.raw.ImageData
import org.scalajs.dom.{document, window}

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext
import scala.scalajs.js.Dynamic
import scala.util.{Failure, Success}

object Capture {

  final case class DetectionModel(
    tiles: Map[Tile, ImageData] = Map()
  )

  private implicit val ex: ExecutionContext = JSExecutionContext.queue
  private var detectionModel = DetectionModel()

  def view(): Unit = {
    startMediaStream()
    val (view, draw) = View.render(detectionModel)
    HTML.addToPage(view)
    draw()
  }

  def addCapture(capture: (Tile, ImageData)): Unit = {
    detectionModel = detectionModel.copy(tiles = detectionModel.tiles.updated(capture._1, capture._2))
    view()
  }

  def startMediaStream(): Unit = {
    val mediaDevices = window.navigator.asInstanceOf[Dynamic].mediaDevices.asInstanceOf[MediaDevices]

    mediaDevices.getUserMedia(MediaStreamConstraints(video=true,audio=false)).toFuture.onComplete {
      case Failure(ex) => window.console.error("Unable to get user media [%s]", Seq(ex))
      case Success(mediaStream) =>
        val videoElement = document.getElementById("media-stream").asInstanceOf[Dynamic]
        videoElement.srcObject = mediaStream
    }
  }

}
