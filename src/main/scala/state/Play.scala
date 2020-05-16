package state

import model.Mahjong.Game

object Play {

  def nextRound(game: Game): Game = {
    game.players.foldRight(game) { case ((playerWind, player), state) =>
      val tilesForPlayer = state.wall.living.take(14)
      val newWall = state.wall.living.drop(14)
      state.copy(
        players=state.players.updated(playerWind, player.copy(hand=player.hand.copy(concealedTiles=tilesForPlayer))),
        wall=state.wall.copy(living=newWall)
      )
    }
  }

}
