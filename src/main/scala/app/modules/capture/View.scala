package app.modules.capture

import app.modules.capture.ImageProcessing._
import app.{App, HTML}
import org.scalajs.dom.html.{Canvas, Video}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.dom.{CanvasRenderingContext2D, document}
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object View {

  private var horizontalBounds = 0;
  private var verticalBounds = 0;

  private def readForm: Unit = {
    val canvas = document.getElementById("media-canvas").asInstanceOf[Canvas]
    val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    val tile = HTML.inputValue("tile-select")
    val image = context.getImageData(0, 0, canvas.width, canvas.height)

    println(tile)
    println(image)
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
            label(`for` := "h-size", id := "h-size-label")(s"Horizontal window ($horizontalBounds)"),
            input(cls := "form-control", `type` := "range", min := 0, max := 160, id := "h-size", value := horizontalBounds, onchange := {(e: Event) =>
              HTML.inputValue("h-size").map(_.toInt).foreach(horizontalBounds = _)
              HTML.updateLabel("h-size-label", s"Horizontal window ($horizontalBounds)")
              redraw()
            })
          ),
          div(cls := "form-group")(
            label(`for` := "v-size", id := "v-size-label")(s"Vertial window ($verticalBounds)"),
            input(cls := "form-control", `type` := "range", min := 0, max := 120, id := "v-size", value := verticalBounds, onchange := {(e: Event) =>
              HTML.inputValue("v-size").map(_.toInt).foreach(verticalBounds = _)
              HTML.updateLabel("v-size-label", s"Horizontal window ($verticalBounds)")
              redraw()
            })
          ),
          div(cls := "form-group")(
            label(`for` := "tile-select")(s"Tile"),
            select(cls := "form-control", id := "tile-select")(
              option()("1 of Circles"),
              option()("2 of Circles"),
            )
          ),
          button(cls := "btn btn-success", onclick := (() => readForm))("Capture"),
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
    drawBox()
    val videoElement = document.getElementById("media-stream").asInstanceOf[Video]
    val canvas = document.getElementById("tile-target").asInstanceOf[Canvas]
    val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    context.drawImage(
      videoElement,
      (320 - horizontalBounds),
      (240 - verticalBounds),
      640,
      480,
      0,
      0,
      320,
      240
    )

    val i = context.getImageData(0, 0, canvas.width, canvas.height)
    i.grayscale()

    context.putImageData(i, 0, 0)
  }

  private def drawBox(): Unit = {
    val canvas = document.getElementById("media-cover").asInstanceOf[Canvas]
    val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    context.clearRect(0, 0, canvas.width, canvas.height)
    context.strokeStyle = "red"
    context.beginPath()
    context.moveTo(320 - horizontalBounds, 0)
    context.lineTo(320 - horizontalBounds, 240)
    context.moveTo(horizontalBounds, 0)
    context.lineTo(horizontalBounds, 240)
    context.moveTo(0, 240 - verticalBounds)
    context.lineTo(320, 240 - verticalBounds)
    context.moveTo(0,verticalBounds)
    context.lineTo(320,verticalBounds)
    context.stroke()
  }

}
