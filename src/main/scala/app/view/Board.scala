package app.view

import model.Mahjong.{East, Game, Hand, North, South, West}
import scalatags.Text
import scalatags.Text.all._
import Rendering._

object Board {

  def view(game: Game): Text.TypedTag[String] = {
    val board = div(cls := "row")(
      div(cls := "col-md-8 mx-auto")(
        canvas(id := "board", widthA := 600, heightA := 600),
      )
    )

    val controls = game.state match {
      case model.Mahjong.NewGame => button(cls := "btn btn-outline-primary", onclick := "Mahjong.next()")("Start game")
      case _ => div()
    }

    div(board, controls)
  }

  def draw(game: Game): Unit = {
    val board = renderOn("board")
    board.translate(0, 0)

    board.fill("green")

    board.translate(300.5, 300.5)

    board.drawCompass(game.round.wind, game.round.activePlayer)
    board.drawWall(game.wall.living.length + game.wall.dead.length)

    board.drawPlayer(game.players(East))
    board.rotate(Math.PI * 0.5)
    board.drawPlayer(game.players(South))
    board.rotate(Math.PI * 0.5)
    board.drawPlayer(game.players(West))
    board.rotate(Math.PI * 0.5)
    board.drawPlayer(game.players(North))
  }

}

