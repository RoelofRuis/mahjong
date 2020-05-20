package app.view.component

import model.Mahjong._
import org.scalajs.dom
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas

object Render {

  def on(id: String): CanvasRenderingContext2D = {
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

  implicit class WindDirectionLetters(windDirection: WindDirection) {
    def asChar: String = {
      windDirection match {
        case West => "W"
        case East => "E"
        case North => "N"
        case South => "S"
      }
    }
  }

}
