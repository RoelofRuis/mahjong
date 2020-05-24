package app.view.component

import app.App
import model.Actions.Discard
import model.Mahjong.Game
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object TileReceivedForm {

  import model.MahjongOps._
  import model.PlayerActions._
  import model.Text._

  def render(model: Game): TypedTag[HTMLElement] = {
    val actionsWithText = model.activePlayer.map(_.validActionsOnReceive).getOrElse(Seq()).flatMap {
      case action @ Discard(tile) => Some(action, tile.asText)
      case _ => None
    }

    table(cls := "table")(
      tbody()(
        tr()(
          th("Received a tile")
        ),
        actionsWithText.map { case (action, text) =>
          tr()(
            td()(text),
            td()(
              button(cls := "btn btn-sm btn-outline-secondary", onclick := { () => App.react(action) })("Discard")
            )
          )
        }
      )
    )
  }

}
