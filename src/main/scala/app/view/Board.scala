package app.view

import app.view.Rendering._
import model.Mahjong._
import scalatags.Text
import scalatags.Text.all._

object Board {

  def view(game: Game): Text.TypedTag[String] = {
    val board = canvas(id := "board", widthA := 600, heightA := 600)

    val controls = game.state match {
      case model.Mahjong.NewGame => button(cls := "btn btn-sm btn-outline-success", onclick := "Mahjong.transition()")("Start game")

      case model.Mahjong.TileReceived =>
        table(cls := "table")(
          tbody()(
            game.players(game.round.activePlayer).hand.concealedTiles.zipWithIndex.map { case (tile, i) =>
              tr()(
                th()(tile.toString),
                th()(
                  button(cls := "btn btn-sm btn-outline-secondary", onclick := s"Mahjong.reactToReceive($i)")("Discard")
                )
              )
            }
          )
        )

      case model.Mahjong.TileDiscarded =>
        if (game.activePlayer.wind == East) button(cls := "btn btn-sm btn-outline-success", onclick := "Mahjong.reactToDiscard()")("Continue")
        else button(cls := "btn btn-sm btn-outline-success", onclick := "Mahjong.reactToDiscard()")("Continue") // TODO: react if not you

      case _ => p()("Not implemented")
    }

    div(cls := "row")(
      div(cls := "col-4")(controls),
      div(cls := "col-8")(board),
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

