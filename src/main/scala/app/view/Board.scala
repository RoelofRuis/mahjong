package app.view

import model.Mahjong.{East, Game, Hand, North, South, West}
import scalatags.Text
import scalatags.Text.all._
import app.Rendering._

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

    def drawPlayerHand(hand: Hand): Unit = {
      val offset = (hand.concealedTiles.length * 6)
      hand.concealedTiles.zipWithIndex.foreach { case (tile, pos) =>
        board.drawTile(-offset + (pos * 12), 276, tile)
      }
    }

    board.translate(300.5, 300.5)
    drawPlayerHand(game.players(East).hand)
    Range(0, 17).foreach { pos => board.drawTileFaceDown(-102 + (pos * 12), 102) }

    board.rotate(Math.PI * 0.5)
    drawPlayerHand(game.players(South).hand)
    Range(0, 17).foreach { pos => board.drawTileFaceDown(-102 + (pos * 12), 102) }

    board.rotate(Math.PI * 0.5)
    drawPlayerHand(game.players(West).hand)
    Range(0, 17).foreach { pos => board.drawTileFaceDown(-102 + (pos * 12), 102) }

    board.rotate(Math.PI * 0.5)
    drawPlayerHand(game.players(North).hand)
    Range(0, 17).foreach { pos => board.drawTileFaceDown(-102 + (pos * 12), 102) }
  }



}
