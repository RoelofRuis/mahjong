package app.view

import app.App
import app.view.component._
import model.Actions.Restart
import model.Mahjong._
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object View {

  def render(model: Game): (Map[String, TypedTag[HTMLElement]], Game => Unit) = {
    val controls = model.state match {
      case Uninitialized => NewGameForm.render(model)
      case TileReceived => TileReceivedForm.render(model)
      case TileDiscarded(_) => TileDiscardedForm.render(model)
      case Ended => p()(s"Game has ended, <display scores> [$model]")
      case _ => p()(s"Not implemented [$model]")
    }

    val contents = Map(
      "nav" -> navContents(),
      "page" -> pageContents(controls),
    )

    (contents, Board.draw)
  }

  private def navContents(): TypedTag[HTMLElement] = div(cls := "navbar navbar-expand-lg navbar-light bg-light")(
    div(cls :="navbar-brand")("Mahjong"),
    button(cls := "btn btn-sm btn-outline-danger ml-auto", onclick := (() => App.react(Restart)))("Restart")
  )

  private def pageContents(controls: TypedTag[HTMLElement]): TypedTag[HTMLElement] = div(cls := "row")(
    div(cls := "col-4")(controls),
    div(cls := "col-8")(canvas(id := "board", widthA := 600, heightA := 600)),
  )

}


