package app.modules.capture

import app.HTML
import org.scalajs.dom.html.Canvas
import typings.std.global.{console, document, window}
import typings.std.{CanvasRenderingContext2D, ImageData, MediaDevices, MediaStreamConstraints}
import typings.w3cImageCapture.global.ImageCapture

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext
import scala.scalajs.js.Dynamic
import scala.util.{Failure, Success}

object Capture {

  private implicit val ex: ExecutionContext = JSExecutionContext.queue

  private var imageCapture: Option[ImageCapture] = None

  def render(): Unit = {
    startMediaStream()
    HTML.addToPage(View.render())
  }

  def startMediaStream(): Unit = {
    typings.std.global.window
    val mediaDevices = window.navigator.asInstanceOf[Dynamic].mediaDevices.asInstanceOf[MediaDevices]

    mediaDevices.getUserMedia(MediaStreamConstraints(video=true,audio=false)).toFuture.onComplete {
      case Failure(ex) => console.error("Unable to get user media [%s]", Seq(ex))
      case Success(mediaStream) =>
        val videoElement = document.getElementById("media-stream").asInstanceOf[Dynamic]
        videoElement.srcObject = mediaStream

        val track = mediaStream.getVideoTracks()(0)
        imageCapture = Some(new ImageCapture(track))
    }
  }

  def captureImage(): Unit = {
    imageCapture match {
      case None => console.error("ImageCapture was not initialized")
      case Some(imageCapture) =>
        imageCapture.grabFrame().toFuture.onComplete {
          case Failure(ex) => console.error("Unable to grab frame [%s]", Seq(ex))
          case Success(imageBitmap) =>
            val canvas = document.getElementById("media-canvas").asInstanceOf[Canvas]
            val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
            context.drawImage(imageBitmap, 0, 0, canvas.width, canvas.height)
            imageBitmap.close()
            val x: ImageData = context.getImageData(0, 0, canvas.width, canvas.height)
        }
    }
  }

}
