package app.modules.capture

import app.HTML
import org.scalajs.dom.experimental.mediastream.{MediaDevices, MediaStreamConstraints}
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.{CanvasRenderingContext2D, document, window}

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext
import scala.scalajs.js.Dynamic
import scala.util.{Failure, Success}

object Capture {

  private implicit val ex: ExecutionContext = JSExecutionContext.queue

  def view(): Unit = {
    startMediaStream()
    val (view, draw) = View.render()
    HTML.addToPage(view)
    draw()
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
        context.strokeStyle = "red"
        context.beginPath()
        context.moveTo(0, 120)
        context.lineTo(340, 120)
        context.moveTo(160, 0)
        context.lineTo(160, 240)
        context.stroke()
    }
  }

}
