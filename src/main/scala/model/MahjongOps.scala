package model

import model.Mahjong.{Game, Hand, Player, WIND_ORDER, WindDirection}

object MahjongOps {

  implicit class GameOps(game: Game) {
    def activePlayer: Player = game.players(game.round.activePlayer) // TODO: this is dangerous as when there are no players..!
    def nextPlayerWind: WindDirection = WIND_ORDER((WIND_ORDER.indexOf(game.round.activePlayer) + 1) % 4)
    def activeHand: Hand = activePlayer.hand
  }

}
