package app.view

import app.view.Rendering._
import model.Mahjong._
import scalatags.Text
import scalatags.Text.all._

object Board {

  def view(game: Game): Text.TypedTag[String] = {
    val board = div(cls := "col-md-8 mx-auto")(
      canvas(id := "board", widthA := 600, heightA := 600),
    )

    val controls = game.state match {
      case model.Mahjong.NewGame => div(cls := "col-md8")(
        button(cls := "btn btn-sm btn-outline-success", onclick := "Mahjong.next()")("Start game")
      )
      case _ => div()
    }

    div(
      hr(),
      div(cls := "row")(board),
      hr(),
      div(cls := "row")(controls),
    )
  }

  def draw(game: Game): Unit = {
    val board = renderOn("board")
    board.translate(0, 0)

    board.fill("green")

    board.translate(300.5, 300.5)

    board.drawCompass(game.round)
    board.drawWall(game.wall.living.length + game.wall.dead.length)

    board.drawPlayer(game.players.get(East), game.round.activePlayer)
    board.rotate(Math.PI * 0.5)
    board.drawPlayer(game.players.get(South), game.round.activePlayer)
    board.rotate(Math.PI * 0.5)
    board.drawPlayer(game.players.get(West), game.round.activePlayer)
    board.rotate(Math.PI * 0.5)
    board.drawPlayer(game.players.get(North), game.round.activePlayer)
  }

}

