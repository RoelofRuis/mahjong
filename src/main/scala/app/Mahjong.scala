package app

import app.view.{HTML, NewGame}
import model.Mahjong.{East, North, South, West, WindDirection}
import state.Initializer

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.Random

@JSExportTopLevel("Mahjong")
object Mahjong {

  def main(args: Array[String]): Unit = {
    HTML.render(NewGame.view())
  }

  @JSExport("startNewGame")
  def startNewGame(): Unit = {
    NewGame.validate match {
      case (model, None) =>
        val playerMap: Map[WindDirection, String] = Seq(
          (East, model.east),
          (South, model.south),
          (West, model.west),
          (North, model.north)
        ).collect { case (dir, Some(name)) => dir -> name }.toMap

        val game = Initializer.newGame(playerMap, Random)

        println("lieve mensen, het spel gaat beginnen")
      case (model, Some(err)) => HTML.render(NewGame.view(model, err))
    }
  }

}
