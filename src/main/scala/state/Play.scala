package state

import model.Mahjong.{Game, NewGame, NextTurn, State}

import scala.annotation.tailrec

object Play {

  @tailrec
  def transition(game: Game): Game = {
    game.state match {
      case NewGame =>
        val nextState = game
          .dealTiles
          .setState(NextTurn)
        transition(nextState)

      case _ => game //TODO
    }
  }

  implicit class GameActions(game: Game) {

    def setState(state: State): Game = {
      game.copy(state = state)
    }

    def dealTiles: Game = {
      game.players.foldRight(game) { case ((playerWind, player), state) =>
        val tilesForPlayer = state.wall.living.take(13)
        val newWall = state.wall.living.drop(13)
        state.copy(
          players=state.players.updated(playerWind, player.copy(hand=player.hand.copy(concealedTiles=tilesForPlayer))),
          wall=state.wall.copy(living=newWall)
        )
      }
    }
  }

}
