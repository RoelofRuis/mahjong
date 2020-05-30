package app.modules.game.component

import app.modules.game.Game
import app.modules.game.model.Actions.Discard
import app.modules.game.model.Mahjong.Table
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object TileReceivedForm {

  import app.modules.game.model.PlayerActions._
  import app.modules.game.model.Text._

  def render(model: Table): TypedTag[HTMLElement] = {
    val actionsWithText = model.validActionsOnReceive.flatMap {
      case action@Discard(tile) => Some(action, tile.asText)
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
              button(cls := "btn btn-sm btn-outline-secondary", onclick := { () => Game.react(action) })("Discard")
            )
          )
        }
      )
    )
  }

}
