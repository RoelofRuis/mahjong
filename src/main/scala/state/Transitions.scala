package state

import model.Actions.{Action, NewGame}
import model.Mahjong._

object Transitions {

  def react(game: Game, action: Action): Game = {
    (game.state, action) match {
      case (Uninitialized, NewGame(playerNames)) =>
        game
          .seatPlayers(playerNames)
          .dealStartingHands
      case _ => game
    }
  }

  implicit class GameActions(game: Game) {

    def seatPlayers(playerNames: Map[WindDirection, String]): Game = {
      val players = WIND_ORDER.flatMap { windDirection =>
        playerNames.get(windDirection) match {
          case None => None
          case Some(name) =>
            Some(Player(
              name,
              0,
              windDirection,
              Hand(),
            ))
        }
      }.map(player => player.wind -> player).toMap

      game.copy(players=players)
    }

    def activePlayerDiscards(tileIndex: Int): Game = {
      val newHand = game.activeHand.copy(
        concealedTiles=game.activeHand.concealedTiles.zipWithIndex.filter { case (_, i) => i != tileIndex }.map(_._1),
        discards=game.activeHand.discards :+ game.activeHand.concealedTiles(tileIndex)
      )

      game.copy(
        state=TileDiscarded,
        players=game.players.updated(game.round.activePlayer, game.activePlayer.copy(hand=newHand))
      )
    }

    def dealIfMoreTiles: Game = {
      game.wall.living.headOption match {
        case None => game.copy(state=NextRound)
        case Some(tile) =>
          val newHand = game.activePlayer.hand.copy(concealedTiles=game.activePlayer.hand.concealedTiles :+ tile)
          game.copy(
            state=TileReceived,
            players=game.players.updated(game.round.activePlayer, game.activePlayer.copy(hand=newHand)),
            wall=game.wall.copy(living=game.wall.living.tail)
          )
      }
    }

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
  }

}
