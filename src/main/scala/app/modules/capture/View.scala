package app.modules.capture

import app.modules.capture.ImageProcessing._
import app.modules.game.model.Mahjong.{Circles, SuitedTile, Tile}
import app.modules.game.model.Text._
import app.{App, HTML}
import org.scalajs.dom.html.{Canvas, Video}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.dom.{CanvasRenderingContext2D, ImageData, document}
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all.{i, _}

object View {

  private var tiles: Map[(Tile, Int), Option[ImageData]] = Seq(
    SuitedTile(Circles, 1),
    SuitedTile(Circles, 2),
    SuitedTile(Circles, 3),
  ).zipWithIndex.map { case (tile, i) => (tile, i) -> None }.toMap
  private var horizontalBound = 0
  private var verticalBound = 0
  private var selectedTile: Int = 0

  private var FAST_t: Int = 10 // FAST algorithm threshold value
  private var FAST_n: Int = 12 // FAST algorithm contiguous values required
  private val BRIEF_Pairs: Array[((Int, Int), (Int, Int))] = ImageProcessing.calculateBriefPairs(9, 64)

  private def analyse(): Unit = {
    tiles
      .collect { case ((tile, _), Some(image)) => (tile, image) }
      .toSeq
      .foreach { case (tile, i) =>
        val (_, context) = HTML.getCanvas("canvas-" + tile.asText)
        val keyPoints = i.FAST(FAST_t, FAST_n, drawResults=true)
        val descriptors = i.BRIEF(keyPoints, BRIEF_Pairs)
        descriptors.map(_.map { if (_) '1' else '0'}.mkString("")).foreach(println)
        context.putImageData(i, 0, 0)
      }
  }

  private def capture(): Unit = {
    for {
      selectedIndex <- HTML.inputValue("tile-select").flatMap(_.toIntOption)
      tile <- tiles.find { case ((_, index), _) => index == selectedIndex }
    } yield {
      tiles = tiles.updated(tile._1, Some(readImage))
    }
    redraw()
  }

  private def readImage: ImageData = {
    val videoElement = document.getElementById("media-stream").asInstanceOf[Video]
    val (canvas, context) = HTML.getCanvas("tile-target")
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

    context.putImageData(i, 0, 0)
    i
  }

  def render(): (Map[String, TypedTag[HTMLElement]], () => Unit) = {
    val contents = Map(
      "nav" -> navContents(),
      "page" -> pageContents(),
    )
    (contents, () => {
      drawBoxOverlay()
      redraw()
    })
  }

  private def navContents(): TypedTag[HTMLElement] = div(cls := "navbar navbar-expand-lg navbar-light bg-light")(
    div(cls := "navbar-brand")("Mahjong"),
    ul(cls := "navbar-nav")(
      li(cls := "nav-item")(
        a(cls := "nav-link", role := "button", onclick := (() => App.view("game")))("Game")
      ),
      li(cls := "nav-item")(
        a(cls := "nav-link active", role := "button", onclick := (() => App.view("capture")))("Capture")
      )
    ),
  )

  private def pageContents(): TypedTag[HTMLElement] = {
    val tileOptions = tiles.map {
      case ((tile, i), _) =>
        if (i == selectedTile) option(value := i, selected := true)(tile.asText)
        else option(value := i)(tile.asText)
    }.toSeq

    div(
      div(cls := "row")(
        div(cls := "col-4")(
          div(cls := "form")(
            div(cls := "form-group")(
              label(`for` := "h-size", id := "h-size-label")(s"Horizontal bound"),
              input(cls := "form-control", `type` := "range", min := 0, max := 160, id := "h-size", value := horizontalBound, onchange := { (_: Event) =>
                HTML.inputValue("h-size").map(_.toInt).foreach(horizontalBound = _)
                drawBoxOverlay()
              })
            ),
            div(cls := "form-group")(
              label(`for` := "v-size", id := "v-size-label")(s"Vertical bound"),
              input(cls := "form-control", `type` := "range", min := 0, max := 120, id := "v-size", value := verticalBound, onchange := { (_: Event) =>
                HTML.inputValue("v-size").map(_.toInt).foreach(verticalBound = _)
                drawBoxOverlay()
              })
            ),
            div(cls := "form-group")(
              label(`for` := "tile-select")(s"Tile"),
              select(cls := "form-control", id := "tile-select", onchange := { (_: Event) =>
                HTML.inputValue("tile-select").flatMap(_.toIntOption).foreach(selectedTile = _)
              })(tileOptions)
            ),
            div(cls := "form-group")(
              button(cls := "btn btn-success", onclick := (() => capture()))("Capture")
            ),
            div(cls := "form-group")(
              button(cls := "btn btn-success", onclick := (() => analyse()))("Detect"),
            )
          )
        ),
        div(cls := "col-8")(
          div(width := 340, height := 240)(
            video(id := "media-stream", zIndex := 0, position := "absolute", attr("width") := 320, attr("height") := 240, attr("autoplay") := true),
            canvas(id := "media-cover", zIndex := 10, position := "absolute", verticalAlign := "top", attr("width") := 320, attr("height") := 240),
            canvas(id := "tile-target", display := "none", verticalAlign := "top", attr("width") := 320, attr("height") := 240)
          ),
          table(
            tbody()(
              tiles.toSeq.map { case ((tile, _), _) =>
                tr()(
                  td()(tile.asText),
                  td()(
                    canvas(id := "canvas-" + tile.asText)
                  )
                )
              }
            )
          )
        )
      )
    )
  }

  private def redraw(): Unit = {
    tiles
      .flatMap { case ((tile, _), imageData) => imageData.map((tile, _)) }
      .foreach { case (tile, imageData) =>
        val (canvas, context) = HTML.getCanvas("canvas-" + tile.asText)
        canvas.width = imageData.width
        canvas.height = imageData.height
        context.putImageData(imageData, 0, 0)
      }
  }

  private def drawBoxOverlay(): Unit = {
    val (canvas, context) = HTML.getCanvas("media-cover")
    context.clearRect(0, 0, canvas.width, canvas.height)
    context.strokeStyle = "red"
    context.beginPath()
    context.moveTo(320 - horizontalBound, 0)
    context.lineTo(320 - horizontalBound, 240)
    context.moveTo(horizontalBound, 0)
    context.lineTo(horizontalBound, 240)
    context.moveTo(0, 240 - verticalBound)
    context.lineTo(320, 240 - verticalBound)
    context.moveTo(0, verticalBound)
    context.lineTo(320, verticalBound)
    context.stroke()
  }

}
