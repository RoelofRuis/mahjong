package app.modules.game

import app.App
import app.modules.game.component._
import app.modules.game.model.Actions.Restart
import app.modules.game.model.Mahjong._
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object View {

  def render(model: Table): (Map[String, TypedTag[HTMLElement]], () => Unit) = {
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

    (contents, Board.draw(model))
  }

  private def navContents(): TypedTag[HTMLElement] = div(cls := "navbar navbar-expand-lg navbar-light bg-light")(
    div(cls :="navbar-brand")("Mahjong"),
    ul(cls :="navbar-nav")(
      li(cls :="nav-item active")(
        a(cls :="nav-link", role := "button", onclick := (() => App.view("game")))("Game")
      ),
      li(cls :="nav-item")(
        a(cls :="nav-link", role := "button", onclick := (() => App.view("capture")))("Capture")
      )
    ),
    button(cls := "btn btn-sm btn-outline-danger ml-auto", onclick := (() => Game.react(Restart)))("Restart"),
  )

  private def pageContents(controls: TypedTag[HTMLElement]): TypedTag[HTMLElement] = div(cls := "row")(
    div(cls := "col-4")(controls),
    div(cls := "col-8")(canvas(id := "board", widthA := 600, heightA := 600)),
  )

}


