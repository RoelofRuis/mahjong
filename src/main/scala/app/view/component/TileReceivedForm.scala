package app.view.component

import app.App
import model.Actions.Discard
import model.Mahjong.Game
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object TileReceivedForm {

  import model.Text.TileText

  def render(model: Game): TypedTag[HTMLElement] = {
    table(cls := "table")(
      tbody()(
        model.players(model.round.activePlayer).hand.concealedTiles.zipWithIndex.map { case (tile, i) =>
          tr()(
            th()(tile.asText),
            th()(
              button(cls := "btn btn-sm btn-outline-secondary", onclick := { () => App.react(Discard(i)) })("Discard")
            )
          )
        }
      )
    )
  }

}
