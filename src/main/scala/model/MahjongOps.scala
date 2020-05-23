package model

import model.Mahjong._

object MahjongOps {

  implicit class GameOps(game: Game) {
    def activePlayer: Option[Player] = game.players.get(game.activeSeat)

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

    def activePlayerIsAI: Boolean = activePlayer.exists(_.playerType == ComputerControlled)

    def getAIPlayerSeats: Iterable[Seat] = game.players.filter { case (_, p) => p.playerType == ComputerControlled }.keys
  }

  implicit class WallOps(game: Game) {
    private val wall = game.wall
    def takeTiles(n: Int): (Vector[Tile], Wall) = (wall.living.take(n), wall.copy(living=wall.living.drop(n)))
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
