package app.view

import model.Mahjong.{East, Game, Hand, North, South, West}
import scalatags.Text
import scalatags.Text.all._
import app.Rendering._

object Board {

  def view(): Text.TypedTag[String] = {
    div(cls := "row")(
      div(cls := "col-md-8 mx-auto")(
        canvas(id := "board", widthA := 600, heightA := 600)
      )
    )
  }

  def draw(game: Game): Unit = {
    val board = renderOn("board")

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
