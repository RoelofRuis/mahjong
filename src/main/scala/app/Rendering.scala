package app

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

    def drawTile(x: Double, y: Double): Unit = {
      ctx.translate(0.5, 0.5)
      ctx.strokeStyle = "black"
      ctx.fillStyle = "white"
      ctx.moveTo(x, y)
      ctx.lineTo(x + 12, y)
      ctx.lineTo(x + 12, y + 18)
      ctx.lineTo(x, y + 18)
      ctx.lineTo(x, y)

      ctx.fill()
      ctx.stroke()
    }

  }

}
