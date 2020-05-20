package app.view.component

import model.Mahjong._
import org.scalajs.dom
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas

object Board {

  import model.Text.WindDirectionText

  def draw: Game => Unit = game => {
    val canvas = dom.document.getElementById("board").asInstanceOf[Canvas]
    val board = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
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

  implicit class GameDrawing(ctx: CanvasRenderingContext2D) {

    def fill(color: String): Unit = {
      ctx.translate(0, 0)
      ctx.fillStyle = color
      ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
    }

    def drawWall(size: Int): Unit = {
      val wallLengths = Range(0, 4).foldRight((Seq[Int](), Math.ceil(size / 2.0).toInt)) { case (_, (seq, remaining)) =>
        val numTiles = Math.min(remaining, 17)
        (seq :+ numTiles, remaining - numTiles)
      }._1

      wallLengths.foreach { l =>
        Range(17 - l, 17).foreach { pos => drawTileOutline(-102 + (pos * 12), 102) }
        ctx.rotate(Math.PI * 0.5)
      }

      ctx.rotate(-Math.PI * 2)
    }

    def drawPlayer(player: Option[Player], activePlayer: WindDirection): Unit = {
      player match {
        case None =>
        case Some(player) =>
          drawPlayerHand(player.hand)

          ctx.beginPath()
          if (player.seatWind == activePlayer) ctx.font = "bold 12px monospace"
          else ctx.font = "12px monospace"

          ctx.fillStyle = "black"
          ctx.fillText(player.name, -290, 290)
          ctx.stroke()
      }
    }

    def drawPlayerHand(hand: Hand): Unit = {
      val offset = (hand.concealedTiles.length * 6)
      hand.concealedTiles.zipWithIndex.foreach { case (tile, pos) =>
        ctx.drawTile(-offset + (pos * 12), 276, tile)
      }
      hand.discards.zipWithIndex.foreach { case (tile, pos) =>
        val row: Int = pos / 6
        val col: Int = pos % 6
        ctx.drawTile(100 + (col * 12), 150 + (row * 19), tile)
      }
    }

    def drawCompass(round: Round): Unit = {
      val RADIUS = 60
      ctx.strokeStyle = "black"
      ctx.fillStyle = "black"
      ctx.beginPath()
      ctx.arc(0, 0, RADIUS, 0, 2 * Math.PI)
      ctx.stroke()
      ctx.beginPath()
      ctx.arc(0, 0, 14, 0, 2 * Math.PI)
      ctx.moveTo(-10, -10)
      ctx.lineTo(-RADIUS, 0)
      ctx.lineTo(-10, 10)
      ctx.lineTo(0, RADIUS)
      ctx.lineTo(10, 10)
      ctx.lineTo(RADIUS, 0)
      ctx.lineTo(10, -10)
      ctx.lineTo(0, -RADIUS)
      ctx.lineTo(-10, -10)
      ctx.font = "20px monospace"
      val coords = Seq((-5, RADIUS + 16), (RADIUS + 2, 6), (-5, -(RADIUS + 4)), (-(RADIUS + 14), 6))
      model.Mahjong.WIND_ORDER.zip(coords).foreach { case (dir, (x, y)) =>
        ctx.fillStyle = "black"
        ctx.fillText(dir.asChar, x, y)
      }
      ctx.strokeStyle = "black"
      ctx.fillStyle = "black"
      ctx.fillText(round.prevalentWind.asChar, -6, 6)
      ctx.stroke()
    }

    def drawTile(x: Double, y: Double, tile: Tile): Unit = {
      drawTileOutline(x, y)

      tile match {
        case SuitedTile(Bamboos, number) =>
          drawNumber(x, y, number)
          drawBamboo(x, y)
        case SuitedTile(Circles, number) =>
          drawNumber(x, y, number)
          drawCircle(x, y)
        case SuitedTile(Characters, number) =>
          drawNumber(x, y, number)
          drawCharacter(x, y)
        case DragonTile(color) =>
          drawDragon(x, y, color)
        case WindTile(dir) =>
          drawWind(x, y, dir)
      }
    }

    private def drawTileOutline(x: Double, y: Double): Unit = {
      ctx.beginPath()
      ctx.strokeStyle = "black"
      ctx.fillStyle = "white"
      ctx.moveTo(x, y)
      ctx.lineTo(x + 12, y)
      ctx.lineTo(x + 12, y + 19)
      ctx.lineTo(x, y + 19)
      ctx.lineTo(x, y)

      ctx.fill()
      ctx.stroke()
    }

    private def drawCircle(x: Double, y: Double): Unit = {
      ctx.beginPath()
      ctx.strokeStyle = "black"
      ctx.moveTo(x + 10, y + 13)
      ctx.arc(x + 6, y + 13, 4, 0, 360)
      ctx.stroke()
    }

    private def drawBamboo(x: Double, y: Double): Unit = {
      ctx.beginPath()
      ctx.strokeStyle = "black"
      ctx.moveTo(x + 2, y + 17)
      ctx.lineTo(x + 10, y + 8)
      ctx.moveTo(x + 2, y + 13)
      ctx.lineTo(x + 6, y + 17)
      ctx.moveTo(x + 10, y + 12)
      ctx.lineTo(x + 6, y + 8)
      ctx.stroke()
    }

    private def drawCharacter(x: Double, y: Double): Unit = {
      ctx.beginPath()
      ctx.strokeStyle = "black"
      ctx.moveTo(x + 2, y + 10)
      ctx.lineTo(x + 10, y + 10)
      ctx.moveTo(x + 4, y + 10)
      ctx.lineTo(x + 4, y + 17)
      ctx.moveTo(x + 8, y + 10)
      ctx.arc(x + 18, y + 10, 11, 1 * Math.PI, 0.78 * Math.PI, true)
      ctx.stroke()
    }

    private def drawWind(x: Double, y: Double, dir: WindDirection): Unit = {
      ctx.font = "11px monospace"
      ctx.strokeStyle = "blue"
      ctx.strokeText(dir.asChar, x + 3, y + 13)
    }

    private def drawDragon(x: Double, y: Double, color: DragonColor): Unit = {
      color match {
        case Red =>
          ctx.font="11px monospace"
          ctx.fillStyle = "red"
          ctx.strokeStyle = "red"
          ctx.strokeText("R", x + 3, y + 13)
          ctx.fillText("R", x + 3, y + 13)
        case Green =>
          ctx.font="11px monospace"
          ctx.fillStyle = "green"
          ctx.strokeStyle = "green"
          ctx.strokeText("G", x + 3, y + 13)
          ctx.fillText("G", x + 3, y + 13)
        case White =>
          ctx.strokeStyle = "black"
          ctx.strokeRect(x + 2, y + 3, 8, 13)
      }
    }

    private def drawNumber(x: Double, y: Double, num: Int): Unit = {
      ctx.font = "10px monospace"
      ctx.fillStyle = "black"
      ctx.strokeStyle = "black"
      ctx.fillText(num.toString, x + 1, y + 7)
    }

  }

}

