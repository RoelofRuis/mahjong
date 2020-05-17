package state

import model.Mahjong.{Game, NewGame, NextRound, NextTurn, State, TileReceived}

import scala.annotation.tailrec

object Play {

  @tailrec
  def transition(game: Game): Game = {
    game.state match {
      case NewGame => transition(game.dealStartingHands)

      case NextRound =>
        // tally score
        ??? // If more rounds

      case NextTurn =>
        game.wall.living.headOption match {
          case None => game.copy(state=NextRound)
          case Some(tile) =>
            val player = game.players(game.round.activePlayer)
            val newHand = player.hand.copy(concealedTiles = player.hand.concealedTiles :+ tile)
            game.copy(
              state = TileReceived,
              players = game.players.updated(game.round.activePlayer, player.copy(hand = newHand))
            )
        }

      case TileReceived => game // TODO

      case _ => game //TODO
    }
  }

  implicit class GameActions(game: Game) {

    def dealStartingHands: Game = {
      game.players.foldRight(game) { case ((playerWind, player), state) =>
        val tilesForPlayer = state.wall.living.take(13)
        val newWall = state.wall.living.drop(13)
        state.copy(
          state=NextTurn,
          players=state.players.updated(playerWind, player.copy(hand=player.hand.copy(concealedTiles=tilesForPlayer))),
          wall=state.wall.copy(living=newWall)
        )
      }
    }

    def dealTileToActivePlayer: Game = {
      ???
    }
  }

}
