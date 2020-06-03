package app.modules.capture

import app.HTML
import app.modules.capture.ImageProcessing._
import org.scalajs.dom.experimental.mediastream.{MediaDevices, MediaStreamConstraints}
import org.scalajs.dom.html.{Canvas, Video}
import org.scalajs.dom.{CanvasRenderingContext2D, ImageData, document, window}

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext
import scala.scalajs.js.Dynamic
import scala.util.{Failure, Success}

object Capture {

  private implicit val ex: ExecutionContext = JSExecutionContext.queue

  def view(): Unit = {
    startMediaStream()
    HTML.addToPage(View.render())
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

  def captureImage(): Unit = {
    val videoElement = document.getElementById("media-stream").asInstanceOf[Video]
    val canvas = document.getElementById("media-canvas").asInstanceOf[Canvas]
    val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    context.drawImage(videoElement,0, 0, canvas.width, canvas.height)

    val i: ImageData = context.getImageData(0, 0, canvas.width, canvas.height)

    i.grayscale()
    i.blur()

    context.putImageData(i, 0, 0)
  }

}
