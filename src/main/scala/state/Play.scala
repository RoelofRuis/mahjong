package state

import model.Mahjong._

import scala.annotation.tailrec

object Play {

  @tailrec
  def transition(game: Game): Game = {
    game.state match {
      case NewGame => transition(game.dealStartingHands)

      case NextRound =>
        // tally score
        ??? // If more rounds

      case NextTurn => transition(game.dealIfMoreTiles)

      case TileReceived => game

      case _ => ???
    }
  }

  def discard(game: Game, tileIndex: Int): Game = {
    game.activePlayerDiscards(tileIndex)
  }

  def noDiscardReaction(game: Game): Game = {
    transition(game.copy(
      state=NextTurn,
      round=game.round.copy(activePlayer=game.nextPlayerWind)
    ))
  }

  implicit class GameActions(game: Game) {

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
