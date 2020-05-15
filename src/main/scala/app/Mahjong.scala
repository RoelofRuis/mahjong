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

    val game = Initializer.newGame(Vector("Eva", "Jeanette", "Mimi", "Corintha"), Random)

    renderer.drawTile(100, 100, game.wall.living.head)
    renderer.drawTile(100, 120, game.wall.living.tail.head)

//    game.wall.living.zipWithIndex.foreach { case (tile, i) =>
//      renderer.drawTile((i * 12), (i * 12), tile)
//    }

    printDebug(game.prettyPrint)
  }

}
