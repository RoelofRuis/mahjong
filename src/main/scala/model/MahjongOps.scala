package model

import model.Mahjong._

object MahjongOps {

  implicit class GameOps(game: Game) {
    def nextSeat: Game = {
      val filledSeats = game.players.keys.toList
      filledSeats.zipWithIndex.find { case (seat, _) => seat == game.activeSeat } match {
        case None => game
        case Some((_, index)) =>
          val nextIndex = (index + 1) % filledSeats.length
          filledSeats(nextIndex)
          game.copy(activeSeat=filledSeats(nextIndex))
      }
    }
    def setState(state: State): Game = game.copy(state=state)
  }

  implicit class WallOps(game: Game) {
    private val wall = game.wall
    def takeTiles(n: Int): (Vector[Tile], Wall) = (wall.living.take(13), wall.copy(living=wall.living.drop(13)))
  }

  implicit class PlayerOps(game: Game) {
    def addPlayerTiles(seat: Seat, tiles: Vector[Tile]): Players = updateSeat(seat) { player =>
      player.copy(concealedTiles=player.concealedTiles ++ tiles)
    }

    def playerDiscards(seat: Seat, tileIndex: Int): Players = updateSeat(seat) { player =>
      player.copy(
        concealedTiles=player.concealedTiles.zipWithIndex.filter{ case (_, i) => i != tileIndex }.map(_._1),
        discards=player.discards :+ player.concealedTiles(tileIndex),
      )
    }

    private def updateSeat(seat: Seat)(f: Player => Player): Players = {
      game.players.updatedWith(seat) {
        case None => None
        case Some(player) => Some(f(player))
      }
    }
  }

}
