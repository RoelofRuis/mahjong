package app

import app.view.NewGame
import org.scalajs.dom.{document, html}
import org.scalajs.dom.raw.Element

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("Mahjong")
object Mahjong {

  val root: Element = document.getElementById("root")

  def main(args: Array[String]): Unit = {
    root.innerHTML = NewGame.view().render
  }

  @JSExport("startNewGame")
  def startNewGame(): Unit = {
    val player1 = document.getElementById("player-1").asInstanceOf[html.Input].value
    val player2 = document.getElementById("player-2").asInstanceOf[html.Input].value
    val player3 = document.getElementById("player-3").asInstanceOf[html.Input].value
    val player4 = document.getElementById("player-4").asInstanceOf[html.Input].value

    // validate
    root.innerHTML = NewGame.view().render

    println("lieve mensen, het spel gaat beginnen")
  }

}
