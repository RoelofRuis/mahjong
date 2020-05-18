package app.view

import app.App
import org.scalajs.dom.html.Div
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object NewGame {

  final case class NewGameModel(
    east: Option[String] = None,
    south: Option[String] = None,
    west: Option[String] = None,
    north: Option[String] = None
  )

  def validate: (NewGameModel, Option[String]) = {
    val model = NewGameModel(
      HTML.inputValue("player-1"),
      HTML.inputValue("player-2"),
      HTML.inputValue("player-3"),
      HTML.inputValue("player-4")
    )

    val numDefined = Seq(model.east, model.south, model.west, model.north).flatten.size
    if (model.east.isEmpty) (model, Some("The East player is required"))
    else if (numDefined < 3) (model, Some("At least 3 players should be entered"))
    else (model, None)
  }

  def view(model: NewGameModel = NewGameModel(), error: String = ""): TypedTag[Div] = {
    div(cls := "form")(
      div(cls := "form-group")(
        label(`for` := "player-1")("Player 1 (East)"),
        input(cls := "form-control", id := "player-1", value := model.east.getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-2")("Player 2 (South)"),
        input(cls := "form-control", id := "player-2", value := model.south.getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-3")("Player 3 (West)"),
        input(cls := "form-control", id := "player-3", value := model.west.getOrElse(""))
      ),
      div(cls := "form-group")(
        label(`for` := "player-4")("Player 4 (North)"),
        input(cls := "form-control", id := "player-4", value := model.north.getOrElse(""))
      ),
      if (error.nonEmpty) div(cls := "alert alert-danger")(error) else div(),
      button(cls := "btn btn-success", onclick := { () => App.startNewGame() })("New Game")
    )
  }

}
