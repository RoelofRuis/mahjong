package app.view.component

import app.App
import app.view.HTML
import model.Actions.NewGame
import model.Mahjong._
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object NewGameForm {

  private def readForm: Option[NewGame] = {
    val east  = HTML.inputValue("player-1").map((East, _))
    val south = HTML.inputValue("player-2").map((South, _))
    val west  = HTML.inputValue("player-3").map((West, _))
    val north = HTML.inputValue("player-4").map((North, _))
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

  def render(model: Game): TypedTag[HTMLElement] = {
    div(cls := "form")(
      div(cls := "form-group")(
        label(`for` := "player-1")("Player 1 (East)"),
        input(cls := "form-control", id := "player-1", value := model.players.get(East).map(_.name).getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-2")("Player 2 (South)"),
        input(cls := "form-control", id := "player-2", value := model.players.get(South).map(_.name).getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-3")("Player 3 (West)"),
        input(cls := "form-control", id := "player-3", value := model.players.get(West).map(_.name).getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-4")("Player 4 (North)"),
        input(cls := "form-control", id := "player-4", value := model.players.get(North).map(_.name).getOrElse(""))
      ),
      div(id := "player-form-error")(),
      button(cls := "btn btn-success", onclick := { () => readForm.foreach(App.react) })("New Game")
    )
  }

}
