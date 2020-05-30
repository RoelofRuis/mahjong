package app.modules.game.component

import app.modules.game.Game
import app.modules.game.model.Actions.ReactToDiscard
import app.modules.game.model.Mahjong.{DoNothing, Table}
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object TileDiscardedForm {

  import app.modules.game.model.MahjongOps._
  import app.modules.game.model.PlayerActions._
  import app.modules.game.model.Text._

  def render(model: Table): TypedTag[HTMLElement] = {
    val discardText = model.lastDiscard.map { case (player, tile) =>
      s"Player '${player.name}' discards '${tile.asText}'"
    }.getOrElse("Unknown discard...")

    val activePlayer = 0 // TODO: this might change?

    val actionsWithText = model.validActionsOnDiscard(activePlayer).flatMap {
      case action @ DoNothing => Some((action, "Do Nothing"))
      case _ => None
    }

    table(cls := "table")(
      tbody()(
        tr()(
          th(colspan := 2)(discardText)
        ),
        actionsWithText.map { case (action, text) =>
          tr()(
            td()("Do Nothing"),
            td()(
              button(cls := "btn btn-sm btn-outline-success", onclick := { () => Game.react(ReactToDiscard(activePlayer, action)) })(text)
            )
          )
        }
      )
    )
  }

}
