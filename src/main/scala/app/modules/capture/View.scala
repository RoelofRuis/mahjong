package app.modules.capture

import app.modules.capture.ImageProcessing._
import app.{App, HTML}
import org.scalajs.dom.html.{Canvas, Video}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.dom.{CanvasRenderingContext2D, document}
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object View {

  private var horizontalBound = 0;
  private var verticalBound = 0;

  private var FAST_Threshold = 10;
  private var FAST_Contiguous = 12;

  private def readForm(): Unit = {
    val videoElement = document.getElementById("media-stream").asInstanceOf[Video]
    val canvas = document.getElementById("tile-target").asInstanceOf[Canvas]
    val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = 320 - (horizontalBound * 2)
    canvas.height = 240 - (verticalBound * 2)

    context.drawImage(
      videoElement,
      horizontalBound * 2,
      verticalBound * 2,
      640 - (horizontalBound * 2),
      480 - (verticalBound * 2),
      0,
      0,
      320 - horizontalBound,
      240 - verticalBound
    )

    val i = context.getImageData(0, 0, canvas.width, canvas.height)
    i.grayscale()
    i.blur()
    i.FAST(FAST_Threshold, FAST_Contiguous)

    context.putImageData(i, 0, 0)
  }

  def render(): (Map[String, TypedTag[HTMLElement]], () => Unit) = {
    val contents = Map(
      "nav" -> navContents(),
      "page" -> pageContents(),
    )
    (contents, redraw)
  }

  private def navContents(): TypedTag[HTMLElement] = div(cls := "navbar navbar-expand-lg navbar-light bg-light")(
    div(cls :="navbar-brand")("Mahjong"),
    ul(cls :="navbar-nav")(
      li(cls :="nav-item")(
        a(cls :="nav-link", role := "button", onclick := (() => App.view("game")))("Game")
      ),
      li(cls :="nav-item")(
        a(cls :="nav-link active", role := "button", onclick := (() => App.view("capture")))("Capture")
      )
    ),
  )

  private def pageContents(): TypedTag[HTMLElement] = div(
    div(cls := "row")(
      div(cls := "col-4")(
        div(cls := "form")(
          div(cls := "form-group")(
            label(`for` := "h-size", id := "h-size-label")(s"Horizontal bound"),
            input(cls := "form-control", `type` := "range", min := 0, max := 160, id := "h-size", value := horizontalBound, onchange := {(e: Event) =>
              HTML.inputValue("h-size").map(_.toInt).foreach(horizontalBound = _)
              redraw()
            })
          ),
          div(cls := "form-group")(
            label(`for` := "v-size", id := "v-size-label")(s"Vertical bound"),
            input(cls := "form-control", `type` := "range", min := 0, max := 120, id := "v-size", value := verticalBound, onchange := {(e: Event) =>
              HTML.inputValue("v-size").map(_.toInt).foreach(verticalBound = _)
              redraw()
            })
          ),
          div(cls := "form-group")(
            label(`for` := "fast-t", id := "v-size-label")(s"FAST threshold"),
            input(cls := "form-control", `type` := "range", id := "fast-t", min := 1, max := 20, value := FAST_Threshold, onchange := {(e: Event) =>
              HTML.inputValue("fast-t").map(_.toInt).foreach(FAST_Threshold = _)
            })
          ),
          div(cls := "form-group")(
            label(`for` := "fast-n", id := "v-size-label")(s"FAST contiguous"),
            input(cls := "form-control", `type` := "range", id := "fast-n", min := 1 , max := 16, value := FAST_Contiguous, onchange := {(e: Event) =>
              HTML.inputValue("fast-n").map(_.toInt).foreach(FAST_Contiguous = _)
            })
          ),
          div(cls := "form-group")(
            label(`for` := "tile-select")(s"Tile"),
            select(cls := "form-control", id := "tile-select")(
              option()("1 of Circles"),
              option()("2 of Circles"),
            )
          ),
          button(cls := "btn btn-success", onclick := (() => readForm()))("Capture"),
        )
      ),
      div(cls := "col-8")(
        div(width := 340, height := 240)(
          video(id := "media-stream", zIndex := 0, position := "absolute", attr("width") := 320, attr("height") := 240, attr("autoplay") := true),
          canvas(id := "media-cover", zIndex := 10, position := "absolute", verticalAlign := "top", attr("width") := 320, attr("height") := 240)
        ),
        canvas(id := "tile-target", verticalAlign := "top", attr("width") := 320, attr("height") := 240)
      )
    )
  )

  private def redraw(): Unit = {
    val canvas = document.getElementById("media-cover").asInstanceOf[Canvas]
    val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    context.clearRect(0, 0, canvas.width, canvas.height)
    context.strokeStyle = "red"
    context.beginPath()
    context.moveTo(320 - horizontalBound, 0)
    context.lineTo(320 - horizontalBound, 240)
    context.moveTo(horizontalBound, 0)
    context.lineTo(horizontalBound, 240)
    context.moveTo(0, 240 - verticalBound)
    context.lineTo(320, 240 - verticalBound)
    context.moveTo(0,verticalBound)
    context.lineTo(320,verticalBound)
    context.stroke()
  }

}
