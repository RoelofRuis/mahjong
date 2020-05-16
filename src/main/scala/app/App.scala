package app

import app.view.{Board, HTML, NewGame}
import model.Mahjong.{East, North, South, West, WindDirection}
import state.Initializer

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.Random

@JSExportTopLevel("Mahjong")
object App {

  def main(args: Array[String]): Unit = {
    Storage.loadGame() match {
      case None =>
        HTML.render(NewGame.view())

      case Some(game) =>
        HTML.render(Board.view())
        Board.draw(game)
    }
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
        Storage.saveGame(game)
        HTML.render(Board.view())
        Board.draw(game)

      case (model, Some(err)) => HTML.render(NewGame.view(model, err))
    }
  }

}
