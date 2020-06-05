package app.modules.capture

import app.modules.capture.Capture.DetectionModel
import app.modules.capture.ImageProcessing._
import app.modules.game.model.Mahjong.{Circles, SuitedTile, Tile}
import app.modules.game.model.Text._
import app.{App, HTML}
import org.scalajs.dom.html.{Canvas, Video}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.dom.{CanvasRenderingContext2D, ImageData, document}
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object View {

  private var horizontalBound = 0;
  private var verticalBound = 0;

  val tiles: Seq[(SuitedTile, Int)] = Seq(
    SuitedTile(Circles, 1),
    SuitedTile(Circles, 2),
    SuitedTile(Circles, 3),
  ).zipWithIndex

  /*
   * private var FAST_Threshold = 10;
   * private var FAST_Contiguous = 12;
   *
   * val BRIEF_Pairs: Array[((Int, Int), (Int, Int))] = ImageProcessing.calculateBriefPairs(9, 64)
   *
   * i.grayscale()
   * i.blur()
   * val keyPoints = i.FAST(FAST_Threshold, FAST_Contiguous, drawResults=true)
   * val descriptors = i.BRIEF(keyPoints, BRIEF_Pairs)
   * descriptors.map(_.map { if (_) '1' else '0'}.mkString("")).foreach(println)
   */

  private def readForm: Option[(Tile, ImageData)] = readTile.map(t => (t, readImage))

  private def readTile: Option[Tile] =
    for {
      selectedIndexString <- HTML.inputValue("tile-select")
      selectedIndex <- selectedIndexString.toIntOption
      tile <- tiles.find { case (_, index) => index == selectedIndex }
    } yield tile._1

  private def readImage: ImageData = {
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

    context.putImageData(i, 0, 0)
    i
  }

  def render(model: DetectionModel): (Map[String, TypedTag[HTMLElement]], () => Unit) = {
    val contents = Map(
      "nav" -> navContents(),
      "page" -> pageContents(model),
    )
    (contents, () => redraw(model))
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

  private def pageContents(model: DetectionModel): TypedTag[HTMLElement] = {
    val tileOptions = tiles.map { case (tile, i) => option(value := i)(tile.asText) }
    div(
      div(cls := "row")(
        div(cls := "col-4")(
          div(cls := "form")(
            div(cls := "form-group")(
              label(`for` := "h-size", id := "h-size-label")(s"Horizontal bound"),
              input(cls := "form-control", `type` := "range", min := 0, max := 160, id := "h-size", value := horizontalBound, onchange := { (e: Event) =>
                HTML.inputValue("h-size").map(_.toInt).foreach(horizontalBound = _)
                drawBoxOverlay()
              })
            ),
            div(cls := "form-group")(
              label(`for` := "v-size", id := "v-size-label")(s"Vertical bound"),
              input(cls := "form-control", `type` := "range", min := 0, max := 120, id := "v-size", value := verticalBound, onchange := { (e: Event) =>
                HTML.inputValue("v-size").map(_.toInt).foreach(verticalBound = _)
                drawBoxOverlay()
              })
            ),
            div(cls := "form-group")(
              label(`for` := "tile-select")(s"Tile"),
              select(cls := "form-control", id := "tile-select")(tileOptions)
            ),
            button(cls := "btn btn-success", onclick := (() => readForm.foreach(Capture.addCapture)))("Capture"),
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
              model.tiles.toSeq.map { case (tile, imageData) =>
                tr()(
                  td()(tile.asText),
                  td()(
                    canvas(id := "canvas-" + tile.asText,attr("width") := imageData.width, attr("height") := imageData.height, src := imageData)
                  )
                )
              }
            )
          )
        )
      )
    )
  }

  private def redraw(model: DetectionModel): Unit = {
    model.tiles.toSeq.foreach { case (tile, imageData) =>
      val canvas = document.getElementById("canvas-" + tile.asText).asInstanceOf[Canvas]
      val context = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
      context.putImageData(imageData, 0, 0)
    }
  }

  private def drawBoxOverlay(): Unit = {
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
    context.moveTo(0, verticalBound)
    context.lineTo(320, verticalBound)
    context.stroke()
  }

}
