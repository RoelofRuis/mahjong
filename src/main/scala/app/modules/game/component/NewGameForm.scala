package app.modules.game.component

import app.HTML
import app.modules.game.Game
import app.modules.game.model.Actions.NewGame
import app.modules.game.model.Mahjong._
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object NewGameForm {

  private def readForm: Option[NewGame] = {
    val east  = HTML.inputValue("player-1").map(name => (0, (HumanControlled, name)))
    val south = HTML.inputValue("player-2").map(name => (1, (ComputerControlled, name)))
    val west  = HTML.inputValue("player-3").map(name => (2, (ComputerControlled, name)))
    val north = HTML.inputValue("player-4").map(name => (3, (ComputerControlled, name)))
    val players = Seq(east, south, west, north).flatten

    val error = if (east.isEmpty) Some("The East player is required")
    else if (players.size < 3) Some("At least 3 players should be entered")
    else None

    error match {
      case Some(err) =>
        HTML.addToPage(Map("player-form-error" -> div(cls := "alert alert-danger")(err)))
        None

      case None =>
        Some(NewGame(players.toMap))
    }
  }

  def render(model: Table): TypedTag[HTMLElement] = {
    div(cls := "form")(
      div(cls := "form-group")(
        label(`for` := "player-1")("Player 1 (East) (Human controlled)"),
        input(cls := "form-control", id := "player-1", value := model.players.get(0).map(_.name).getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-2")("Player 2 (South) (Computer controlled)"),
        input(cls := "form-control", id := "player-2", value := model.players.get(1).map(_.name).getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-3")("Player 3 (West) (Computer controlled)"),
        input(cls := "form-control", id := "player-3", value := model.players.get(2).map(_.name).getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-4")("Player 4 (North) (Computer controlled)"),
        input(cls := "form-control", id := "player-4", value := model.players.get(3).map(_.name).getOrElse(""))
      ),
      div(id := "player-form-error")(),
      button(cls := "btn btn-success", onclick := { () => readForm.foreach(Game.react) })("New Game")
    )
  }

}
