package app

import model.Mahjong.{Bamboos, Characters, Circles, DragonColor, DragonTile, East, Green, North, Red, South, SuitedTile, Tile, West, White, WindDirection, WindTile}
import org.scalajs.dom
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas

object Rendering {

  def rendererOn(id: String): CanvasRenderingContext2D = {
    val canvas = dom.document.getElementById(id).asInstanceOf[Canvas]
    canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
  }

  implicit class Render2DOps(ctx: CanvasRenderingContext2D) {

    def fill(color: String): Unit = {
      ctx.translate(0, 0)
      ctx.fillStyle = color
      ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
    }

  }

  implicit class GameDrawing(ctx: CanvasRenderingContext2D) {

    def drawTile(x: Double, y: Double, tile: Tile): Unit = {
//      ctx.translate(0.5, 0.5)

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

//      ctx.translate(0.0, 0.0)
    }

    def drawTileOutline(x: Double, y: Double): Unit = {
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

    def drawCircle(x: Double, y: Double): Unit = {
      ctx.strokeStyle = "black"
      ctx.moveTo(x + 10, y + 13)
      ctx.arc(x + 6, y + 13, 4, 0, 360)
      ctx.stroke()
    }

    def drawBamboo(x: Double, y: Double): Unit = {
      ctx.strokeStyle = "black"
      ctx.moveTo(x + 2, y + 17)
      ctx.lineTo(x + 10, y + 8)
      ctx.moveTo(x + 2, y + 13)
      ctx.lineTo(x + 6, y + 17)
      ctx.moveTo(x + 10, y + 12)
      ctx.lineTo(x + 6, y + 8)
      ctx.stroke()
    }

    def drawCharacter(x: Double, y: Double): Unit = {
      ctx.strokeStyle = "black"
      ctx.moveTo(x + 2, y + 10)
      ctx.lineTo(x + 10, y + 10)
      ctx.moveTo(x + 4, y + 10)
      ctx.lineTo(x + 4, y + 17)
      ctx.moveTo(x + 8, y + 10)
      ctx.arc(x + 18, y + 10, 11, 1 * Math.PI, 0.78 * Math.PI, true)
      ctx.stroke()
    }

    def drawWind(x: Double, y: Double, dir: WindDirection): Unit = {
      ctx.font = "11px monospace"
      ctx.strokeStyle = "blue"
      val char = dir match {
        case West => 'W'
        case East => 'E'
        case North => 'N'
        case South => 'S'
      }
      ctx.strokeText(char.toString, x + 3, y + 13)
    }

    def drawDragon(x: Double, y: Double, color: DragonColor): Unit = {
      color match {
        case Red =>
          ctx.font="13px monospace"
          ctx.fillStyle = "red"
          ctx.strokeStyle = "red"
          ctx.strokeText("R", x + 3, y + 13)
          ctx.fillText("R", x + 3, y + 13)
        case Green =>
          ctx.font="13px monospace"
          ctx.fillStyle = "green"
          ctx.strokeStyle = "green"
          ctx.strokeText("G", x + 3, y + 13)
          ctx.fillText("G", x + 3, y + 13)
        case White =>
          ctx.strokeStyle = "black"
          ctx.strokeRect(x + 2, y + 3, 8, 13)
      }
    }

    def drawNumber(x: Double, y: Double, num: Int): Unit = {
      ctx.font = "10px monospace"
      ctx.fillStyle = "black"
      ctx.strokeText(num.toString, x + 1, y + 7)
    }

  }

}
