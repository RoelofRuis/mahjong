package app.view

import app.view.component._
import model.Mahjong._
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object View {

  // TODO: add and wire this button
  // <button class="btn btn-sm btn-outline-danger ml-auto" onclick="Mahjong.reset()">Reset</button>

  def render(model: Game): (TypedTag[HTMLElement], Game => Unit) = {
    val controls = model.state match {
      case Uninitialized => NewGameForm.render(model)
      case TileReceived => TileReceivedForm.render(model)
      case TileDiscarded => TileDiscardedForm.render(model)
      case _ => p()(s"Not implemented [${model}]")
    }

    val view = div(cls := "row")(
      div(cls := "col-4")(controls),
      div(cls := "col-8")(canvas(id := "board", widthA := 600, heightA := 600)),
    )

    (view, Board.draw)
  }




}


