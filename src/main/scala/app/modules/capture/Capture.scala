package app.modules.capture

import app.HTML
import app.modules.capture.ImageProcessing._
import app.modules.capture.Model.DetectionModel
import org.scalajs.dom.experimental.mediastream.{MediaDevices, MediaStreamConstraints}
import org.scalajs.dom.html.{Canvas, Video}
import org.scalajs.dom.{CanvasRenderingContext2D, ImageData, document, window}

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext
import scala.scalajs.js.Dynamic
import scala.util.{Failure, Success}

object Capture {

  private implicit val ex: ExecutionContext = JSExecutionContext.queue

  private var model: DetectionModel = DetectionModel(
    320,
    240,
    1.0
  )

  def view(): Unit = {
    startMediaStream()
    HTML.addToPage(View.render(model))
  }

  def setHorizontalWindow(v: Int): Unit = {
    model = model.copy(horizontalWindow = v)
    captureImage()
  }

  def setVerticalWindow(v: Int): Unit = {
    model = model.copy(verticalWindow = v)
    captureImage()
  }

  def setZoom(v: Double): Unit = {
    model = model.copy(zoom = v)
    captureImage()
  }

  def startMediaStream(): Unit = {
    val mediaDevices = window.navigator.asInstanceOf[Dynamic].mediaDevices.asInstanceOf[MediaDevices]

    mediaDevices.getUserMedia(MediaStreamConstraints(video=true,audio=false)).toFuture.onComplete {
      case Failure(ex) => window.console.error("Unable to get user media [%s]", Seq(ex))
      case Success(mediaStream) =>
        val videoElement = document.getElementById("media-stream").asInstanceOf[Dynamic]
        videoElement.srcObject = mediaStream

        val canvas = document.getElementById("media-cover").asInstanceOf[Canvas]
        val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        context.fillStyle = "red"
        context.beginPath()
        context.moveTo(0, 120)
        context.lineTo(340, 120)
        context.moveTo(160, 0)
        context.lineTo(160, 240)
        context.stroke()
    }
  }

  def captureImage(): Unit = {
    val videoElement = document.getElementById("media-stream").asInstanceOf[Video]
    val canvas = document.getElementById("media-canvas").asInstanceOf[Canvas]
    canvas.width = model.horizontalWindow
    canvas.height = model.verticalWindow
    val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    context.fillStyle = "white"
    context.fillRect(0, 0, canvas.width, canvas.height)
    context.drawImage(
      videoElement,
      640 - (640 / model.zoom) + ((320 - model.horizontalWindow) / 2),
      480 - (480 / model.zoom) + ((240 - model.verticalWindow) / 2),
      640 / model.zoom,
      480 / model.zoom,
      0,
      0,
      320 * model.zoom,
      240 * model.zoom
    )

    val i = context.getImageData(0, 0, canvas.width, canvas.height)
    i.grayscale()

    context.putImageData(i, 0, 0)
  }

}
