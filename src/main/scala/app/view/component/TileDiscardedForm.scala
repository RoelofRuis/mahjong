package app.view.component

import app.App
import model.Actions.ReactToDiscard
import model.Mahjong.{DoNothing, Game}
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object TileDiscardedForm {

  def render(model: Game): TypedTag[HTMLElement] = {
    table(cls := "table")(
      tbody()(
        tr()(
          th()("Do Nothing"),
          th()(
            button(cls := "btn btn-sm btn-outline-success", onclick := { () => App.react(ReactToDiscard(0, DoNothing)) })("Do Nothing")
          )
        )
      )
    )
  }

}
