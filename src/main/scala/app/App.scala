package app

import app.modules.capture.Capture
import app.modules.game.Game

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("App")
object App {

  def main(args: Array[String]): Unit = Game.render()

  @JSExport("view")
  def view(viewName: String): Unit = {
    viewName match {
      case "game" => Game.render()
      case "capture" => Capture.render()
    }
  }

}
