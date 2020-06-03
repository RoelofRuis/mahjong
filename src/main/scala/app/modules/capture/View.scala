package app.modules.capture

import app.App
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object View {

  def render(): (Map[String, TypedTag[HTMLElement]]) = {
    Map(
      "nav" -> navContents(),
      "page" -> pageContents(),
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

  private def pageContents(): TypedTag[HTMLElement] = div(
    div(cls := "row")(
      video(id := "media-stream", width := 320, height := 240, attr("autoplay") := true)
    ),
    div(cls := "row")(
      canvas(id := "media-canvas", width := 320, height := 240)
    ),
    div(cls := "row")(
      button(cls := "btn btn-sm btn-primary", onclick := (() => Capture.captureImage()))("Capture"),
    )
  )

}
