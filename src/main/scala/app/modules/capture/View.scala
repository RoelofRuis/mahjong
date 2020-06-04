package app.modules.capture

import app.{App, HTML}
import app.modules.capture.Model.DetectionModel
import org.scalajs.dom.raw.{Event, HTMLElement}
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object View {

  def render(model: DetectionModel): Map[String, TypedTag[HTMLElement]] = {
    Map(
      "nav" -> navContents(),
      "page" -> pageContents(model),
    )
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

  private def pageContents(model: DetectionModel): TypedTag[HTMLElement] = div(
    div(cls := "row")(
      div(cls := "col-4")(
        div(cls := "form")(
          div(cls := "form-group")(
            label(`for` := "h-size")("Horizontal window"),
            input(`type` := "range", min := 50, max := 320, id := "h-size", value := model.horizontalWindow, onchange := {(e: Event) =>
              HTML.inputValue("h-size").map(_.toInt).foreach(Capture.setHorizontalWindow)
            })
          ),
          div(cls := "form-group")(
            label(`for` := "v-size")("Vertial window"),
            input(`type` := "range", min := 50, max := 240, id := "v-size", value := model.verticalWindow, onchange := {(e: Event) =>
              HTML.inputValue("v-size").map(_.toInt).foreach(Capture.setVerticalWindow)
            })
          ),
          div(cls := "form-group")(
            label(`for` := "zoom")("Zoom"),
            input(`type` := "range", step := 0.01, min := 1.0, max := 2.0, id := "zoom", value := model.zoom, onchange := {(e: Event) =>
              HTML.inputValue("zoom").map(_.toDouble).foreach(Capture.setZoom)
            })
          )
        )
      ),
      div(cls := "col-8")(
        video(id := "media-stream", attr("width") := 320, attr("height") := 240, attr("autoplay") := true),
        canvas(id := "media-canvas", verticalAlign := "top", attr("width") := 320, attr("height") := 240)
      )
    )
  )

}
