package app.view.component

import app.App
import model.Actions.ReactToDiscard
import model.Mahjong.{DoNothing, Game}
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object TileDiscardedForm {

  import model.MahjongOps._
  import model.Text._

  def render(model: Game): TypedTag[HTMLElement] = {
    val discardText = model.lastDiscard.map { case (player, tile) =>
      s"Player '${player.name}' discards '${tile.asText}'"
    }.getOrElse("Unknown discard...")

    table(cls := "table")(
      tbody()(
        tr()(
          th(colspan := 2)(discardText)
        ),
        tr()(
          td()("Do Nothing"),
          td()(
            button(cls := "btn btn-sm btn-outline-success", onclick := { () => App.react(ReactToDiscard(0, DoNothing)) })("Do Nothing")
          )
        )
      )
    )
  }

}
