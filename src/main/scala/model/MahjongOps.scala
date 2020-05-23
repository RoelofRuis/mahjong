package model

import model.Mahjong._

object MahjongOps {

  implicit class GameOps(game: Game) {
    def activePlayer: Player = game.players(game.round.activeSeat) // TODO: this is dangerous as when there are no players..!
    def nextSeat: Seat = game.round.activeSeat + 1 % 4 // TODO: this is incorrect for fewer than 4 players!
    def activeHand: Hand = activePlayer.hand
  }

}
