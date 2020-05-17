package app

import app.view.NewGame.NewGameModel
import app.view.{Board, HTML, NewGame}
import model.Mahjong._
import org.scalajs.dom
import state.{Initializer, Play}

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.Random

@JSExportTopLevel("Mahjong")
object App {

  def main(args: Array[String]): Unit = {
    Storage.loadGame() match {
      case Right(Some(game)) =>
        displayGame(game)
      case Right(None) =>
        HTML.render(NewGame.view())
      case Left(err) =>
        HTML.render(NewGame.view(NewGameModel(), err))
    }
  }

  @JSExport("reset")
  def reset(): Unit = {
    if (dom.window.confirm("This will reset the game. You will lose all current progress. Are you sure?")) {
      Storage.removeGame()
      main(Array())
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

        val newGame = Initializer.newGame(playerMap, Random)
        Storage.saveGame(newGame)
        displayGame(newGame)

      case (model, Some(err)) => HTML.render(NewGame.view(model, err))
    }
  }

  @JSExport("next")
  def next(): Unit = {
    Storage.loadGame() match {
      case Right(Some(game)) =>
        val newGame = Play.transition(game)
        Storage.saveGame(newGame)
        displayGame(newGame)

      case _ =>
        HTML.render(NewGame.view(NewGameModel(), "No game data found"))
    }
  }

  private def displayGame(game: Game): Unit = {
    HTML.render(Board.view(game))
    Board.draw(game)
  }

}
