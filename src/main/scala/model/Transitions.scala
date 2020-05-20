package model

import model.Actions._
import model.Mahjong._

import scala.util.Random

object Transitions {

  def react(game: Game, action: Action): Game = {
    (game.state, action) match {
      case (_, Restart) => Game(Random)
      case (Uninitialized, NewGame(playerNames)) => game.seatPlayers(playerNames).dealStartingHands
      case (NextTurn, DealTile) => game.dealIfMoreTiles
      case (TileReceived, Discard(i)) => game.activePlayerDiscards(i)
      case (NextRound, TallyScores) => game.tallyScores.nextRound.dealStartingHands
      case (TileDiscarded, DoNothing) => game.copy(state=NextTurn,round=game.round.copy(activePlayer=game.nextPlayerWind))

      case _ => game
    }
  }

  implicit class GameActions(game: Game) {

    def tallyScores: Game = game // TODO: implement

    def nextRound: Game = {
      if (game.round.prevalentWind == WIND_ORDER.last) game.copy(state=Ended)
      else {
        val tiles = game.wall.dead ++ game.wall.living ++ game.players.flatMap { case (_, p) =>
          p.hand.concealedTiles ++ p.hand.discards
        }

        val shuffled = Random.shuffle(tiles)

        game.copy(
          wall=Wall(shuffled.drop(14), shuffled.take(14)),
          players=game.players.map { case (d, p) => d -> p.copy(hand=Hand()) },
          round=Round(WIND_ORDER(WIND_ORDER.indexOf(game.round.prevalentWind) + 1), WIND_ORDER(0), 0)
        )
      }
    }

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
