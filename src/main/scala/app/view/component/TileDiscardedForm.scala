package app.view.component

import app.App
import model.Actions.DoNothing
import model.Mahjong.Game
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
            button(cls := "btn btn-sm btn-outline-success", onclick := { () => App.react(DoNothing) })("Submit")
          )
        )
      )
    )
  }

}
