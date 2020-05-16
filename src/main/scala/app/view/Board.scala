package app.view

import model.Mahjong.Game
import scalatags.Text
import scalatags.Text.all._
import app.Rendering._

object Board {

  def view(): Text.TypedTag[String] = {
    div(cls := "row")(
      div(cls := "col-md-8 mx-auto")(
        canvas(id := "board", width := 600, height := 600)
      )
    )
  }

  def draw(game: Game): Unit = {
    val board = renderOn("board")

    board.fill("green")
  }

}
