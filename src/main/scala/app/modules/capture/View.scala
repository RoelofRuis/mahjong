package app.modules.capture

import app.modules.capture.ImageProcessing._
import app.modules.game.model.Mahjong.{Circles, SuitedTile, Tile}
import app.modules.game.model.Text._
import app.{App, HTML}
import org.scalajs.dom
import org.scalajs.dom.html.Video
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.dom.{ImageData, document}
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

import scala.scalajs.js.typedarray.Uint8ClampedArray

object View {

  private var tiles: Map[(Tile, Int), Option[ImageData]] = Seq(
    SuitedTile(Circles, 1),
    SuitedTile(Circles, 2),
    SuitedTile(Circles, 3),
  ).zipWithIndex.map { case (tile, i) => (tile, i) -> None }.toMap
  private var horizontalBound: Int = 0
  private var verticalBound: Int = 0
  private var selectedTile: Int = 0

  private var redHueWidth = 30
  private var redSaturation = 0.5

  private var FAST_t: Int = 10 // FAST algorithm threshold value
  private var FAST_n: Int = 12 // FAST algorithm contiguous values required
  private val BRIEF_Pairs: Array[((Int, Int), (Int, Int))] = ImageProcessing.calculateBriefPairs(9, 64)

  private def testModel(): Unit = {
    val videoElement = document.getElementById("media-stream").asInstanceOf[Video]
    val (canvas, context) = HTML.getCanvas("model-verify")
    canvas.removeAttribute("style")

    drawBoxOverlay(0, 90)

    context.drawImage(
      videoElement,
      0,
      180,
      640,
      120,
      0,
      0,
      640,
      120
    )

    val i = context.getImageData(0, 0, canvas.width, canvas.height)
    i.grayscale()
    i.blur()
    i.FAST(FAST_t, FAST_n, drawResults = true)

    context.putImageData(i, 0, 0)
  }

  private def hideTest(): Unit = {
    val (canvas, context) = HTML.getCanvas("model-verify")
    context.clearRect(0, 0, canvas.width, canvas.height)
    canvas.style = "display: none"
    drawBoxOverlay(horizontalBound, verticalBound)
  }

  private def analyse(): Unit = {
    tiles
      .collect { case ((tile, _), Some(image)) => (tile, image) }
      .toSeq
      .foreach { case (tile, i) =>
        val (_, context) = HTML.getCanvas("canvas-" + tile.asText)
        val workImage = context.createImageData(i.width, i.height)
        workImage.data.asInstanceOf[Uint8ClampedArray].set(i.data)
        val keyPoints = workImage.FAST(FAST_t, FAST_n, drawResults = true)
        val descriptors = workImage.BRIEF(keyPoints, BRIEF_Pairs)
        //        descriptors.map(_.map { if (_) '1' else '0'}.mkString("")).foreach(println)
        context.putImageData(workImage, 0, 0)
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
    i.colorThreshold(
      redHueWidth,
      redSaturation
    )

    context.putImageData(i, 0, 0)
    i
  }

  private def reloadTiles(): Unit = {
    load() match {
      case None =>
      case Some(loadedTiles) =>
        val resultMap = loadedTiles.toMap
        tiles = tiles.map { case ((tile, index), image) =>
          resultMap.get(tile.asText) match {
            case None => ((tile, index), image)
            case Some((width, height, base64String)) =>
              val bytes = dom.window.atob(base64String).map(_.toInt)
              val (canvas, context) = HTML.getCanvas("canvas-" + tile.asText)
              canvas.width = width
              canvas.height = height
              val img = context.getImageData(0, 0, width, height)
              for (b <- bytes.indices) {
                img.data(b * 4) = bytes(b)
                img.data(b * 4 + 1) = bytes(b)
                img.data(b * 4 + 2) = bytes(b)
                img.data(b * 4 + 3) = 255
              }
              ((tile, index), Some(img))
          }
        }
        redraw()
    }
  }

  def render(): (Map[String, TypedTag[HTMLElement]], () => Unit) = {
    val contents = Map(
      "nav" -> navContents(),
      "page" -> pageContents(),
    )
    (contents, () => {
      drawBoxOverlay(horizontalBound, verticalBound)
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
          div(width := 340, height := 240)(
            video(id := "media-stream", zIndex := 0, position := "absolute", attr("width") := 320, attr("height") := 240, attr("autoplay") := true),
            canvas(id := "media-cover", zIndex := 10, position := "absolute", verticalAlign := "top", attr("width") := 320, attr("height") := 240),
            canvas(id := "tile-target", display := "none", verticalAlign := "top", attr("width") := 320, attr("height") := 240)
          ),
          div(cls := "form")(
            div(cls := "form-group")(
              label(`for` := "h-size", id := "h-size-label")(s"Horizontal bound"),
              input(cls := "form-control", `type` := "range", min := 0, max := 160, id := "h-size", value := horizontalBound, onchange := { (_: Event) =>
                HTML.inputValue("h-size").map(_.toInt).foreach(horizontalBound = _)
                drawBoxOverlay(horizontalBound, verticalBound)
              })
            ),
            div(cls := "form-group")(
              label(`for` := "v-size", id := "v-size-label")(s"Vertical bound"),
              input(cls := "form-control", `type` := "range", min := 0, max := 120, id := "v-size", value := verticalBound, onchange := { (_: Event) =>
                HTML.inputValue("v-size").map(_.toInt).foreach(verticalBound = _)
                drawBoxOverlay(horizontalBound, verticalBound)
              })
            ),
            div(cls := "form-group")(
              label(`for` := "r-hue-width", id := "r-hue-width-label")(s"Red Hue Width"),
              input(cls := "form-control", `type` := "range", min := 0, max := 45, id := "r-hue-width", value := redHueWidth, onchange := { (_: Event) =>
                HTML.inputValue("r-hue-width").map(_.toInt).foreach(redHueWidth = _)
              })
            ),
            div(cls := "form-group")(
              label(`for` := "r-sat", id := "r-sat-label")(s"Red Saturation"),
              input(cls := "form-control", `type` := "range", min := 0, max := 1, step := 0.1, id := "r-sat", value := redSaturation, onchange := { (_: Event) =>
                HTML.inputValue("r-sat").map(_.toDouble).foreach(redSaturation = _)
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
              label(`for` := "fast-t", id := "fast-t-label")(s"FAST t"),
              input(cls := "form-control", `type` := "range", min := 1, max := 20, id := "fast-t", value := FAST_t, onchange := { (_: Event) =>
                HTML.inputValue("fast-t").map(_.toInt).foreach(FAST_t = _)
              })
            ),
            div(cls := "form-group")(
              label(`for` := "fast-n", id := "fast-n-label")(s"FAST n"),
              input(cls := "form-control", `type` := "range", min := 1, max := 16, id := "fast-n", value := FAST_n, onchange := { (_: Event) =>
                HTML.inputValue("fast-n").map(_.toInt).foreach(FAST_n = _)
              })
            ),
            div(cls := "form-group")(
              button(cls := "btn btn-success mr-1", onclick := (() => analyse()))("Analyse"),
              button(cls := "btn btn-success mr-1", onclick := (() => testModel()))("Test"),
              button(cls := "btn btn-outline-success", onclick := (() => hideTest()))("Hide"),
            ),
            div(cls := "form-group")(
              button(cls := "btn btn-primary mr-1 ", onclick := (() => save(tiles)))("Save"),
              button(cls := "btn btn-primary", onclick := (() => reloadTiles()))("Load")
            )
          )
        ),
        div(cls := "col-8")(
          canvas(id := "model-verify", display := "none", attr("width") := 640, attr("height") := 120),
          table(cls := "table table-bordered")(
            tbody()(
              tiles.toSeq.map { case ((tile, _), _) =>
                tr()(
                  td()(tile.asText),
                  td()(
                    canvas(id := "canvas-" + tile.asText, attr("height") := 0)
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

  private def drawBoxOverlay(hBound: Int, vBound: Int): Unit = {
    val (canvas, context) = HTML.getCanvas("media-cover")
    context.clearRect(0, 0, canvas.width, canvas.height)
    context.strokeStyle = "red"
    context.beginPath()
    context.moveTo(320 - hBound, 0)
    context.lineTo(320 - hBound, 240)
    context.moveTo(hBound, 0)
    context.lineTo(hBound, 240)
    context.moveTo(0, 240 - vBound)
    context.lineTo(320, 240 - vBound)
    context.moveTo(0, vBound)
    context.lineTo(320, vBound)
    context.stroke()
  }

}
