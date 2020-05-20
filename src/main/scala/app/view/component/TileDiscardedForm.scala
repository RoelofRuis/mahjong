package app.view.component

import app.App
import model.Actions.DoNothing
import model.Mahjong.{East, Game}
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object TileDiscardedForm {

  def render(model: Game): TypedTag[HTMLElement] = {
    if (model.activePlayer.wind == East) button(cls := "btn btn-sm btn-outline-success", onclick := { () => App.react(DoNothing) })("Continue")
    else button(cls := "btn btn-sm btn-outline-success", onclick := { () => App.react(DoNothing) })("Continue") // TODO: react if not you
  }

}
