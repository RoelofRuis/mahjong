package app

import app.view.NewGame.NewGameModel
import app.view.{Board, HTML, NewGame}
import model.Mahjong.{East, Game, North, South, West, WindDirection}
import state.{Initializer, Play}

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.Random

@JSExportTopLevel("Mahjong")
object App {

  def main(args: Array[String]): Unit = {
    Storage.loadGame() match {
      case Right(Some(game)) =>
        HTML.render(Board.view())
        Board.draw(game)
      case Right(None) =>
        HTML.render(NewGame.view())
      case Left(err) =>
        HTML.render(NewGame.view(NewGameModel(), err))
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

        playGame(Initializer.newGame(playerMap, Random))

      case (model, Some(err)) => HTML.render(NewGame.view(model, err))
    }
  }

  private def playGame(uninitializedGame: Game): Unit = {
    val game = Play.nextRound(uninitializedGame)
    Storage.saveGame(game)
    HTML.render(Board.view())
    Board.draw(game)
  }

}
