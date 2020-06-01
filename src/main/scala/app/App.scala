package app

import app.modules.capture.Capture
import app.modules.game.Game

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("App")
object App {

  def main(args: Array[String]): Unit = Game.view()

  def view(viewName: String): Unit = {
    viewName match {
      case "game"    => Game.view()
      case "capture" => Capture.view()
    }
  }

}
