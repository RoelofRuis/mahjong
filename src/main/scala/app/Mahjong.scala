package app

import state.Initializer
import Util.printDebug
import app.Rendering._
import model.Debugger._

import scala.util.Random

object Mahjong {

  def main(args: Array[String]): Unit = {
    val renderer = rendererOn("canvas")

    renderer.fill("green")

    renderer.drawTile(200, 400)

    val game = Initializer.newGame(Vector("Eva", "Jeanette", "Mimi", "Corintha"), Random)

    printDebug(game.prettyPrint)
  }

}
