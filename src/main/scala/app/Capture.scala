package app

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext
import scala.scalajs.js.Dynamic
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}
import typings.std.global.{console, document, window}
import typings.std.{MediaDevices, MediaStreamConstraints}
import typings.w3cImageCapture.global.ImageCapture

@JSExportTopLevel("Capture")
object Capture {

  private implicit val ex: ExecutionContext = JSExecutionContext.queue

  private var imageCapture: Option[ImageCapture] = None

  @JSExport("startMediaStream")
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

  @JSExport("captureImage")
  def captureImage(): Unit = {
    imageCapture match {
      case None => console.error("ImageCapture was not initialized")
      case Some(imageCapture) =>
        imageCapture.grabFrame().toFuture.onComplete {
          case Failure(ex) => console.error("Unable to grab frame [%s]", Seq(ex))
          case Success(imageBitmap) =>
            console.log("Captured image! [%s]", Seq(imageBitmap))
            imageBitmap.close()
        }
    }
  }

}

