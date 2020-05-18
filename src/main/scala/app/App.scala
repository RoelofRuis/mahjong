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
        HTML.addToPage(NewGame.view())
      case Left(err) =>
        HTML.addToPage(NewGame.view(NewGameModel(), err))
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

      case (model, Some(err)) => HTML.addToPage(NewGame.view(model, err))
    }
  }

  @JSExport("transition")
  def transition(): Unit = withLoadedGame { game =>
    val nextState = Play.transition(game)
    Storage.saveGame(nextState)
    displayGame(nextState)
  }

  @JSExport("reactToReceive")
  def reactToReceive(i: Int): Unit = withLoadedGame { game =>
    val nextState = Play.discard(game, i)
    Storage.saveGame(nextState)
    displayGame(nextState)
  }

  @JSExport("reactToDiscard")
  def reactToDiscard(): Unit = withLoadedGame { game =>
    val nextState = Play.noDiscardReaction(game)
    Storage.saveGame(nextState)
    displayGame(nextState)
  }

  private def withLoadedGame(f: Game => Unit): Unit = {
    Storage.loadGame() match {
      case Right(Some(game)) => f(game)
      case _ => HTML.addToPage(NewGame.view(NewGameModel(), "No game data found"))
    }
  }

  private def displayGame(game: Game): Unit = {
    HTML.addToPage(Board.view(game))
    Board.draw(game)
  }

}
