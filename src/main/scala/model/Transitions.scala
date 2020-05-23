package model

import model.Actions._
import model.Mahjong._

import scala.util.Random

object Transitions {

  import MahjongOps._

  def react(game: Game, action: Action): Game = {
    (game.state, action) match {
      case (_, Restart) => Mahjong.newGame(Random)
      case (Uninitialized, NewGame(playerData)) => game.seatPlayers(playerData).dealStartingHands
      case (NextTurn, DealTile) => game.dealIfMoreTiles
      case (TileReceived, Discard(i)) => game.activePlayerDiscards(i)
      case (NextRound, TallyScores) => game.tallyScores.nextRound.dealStartingHands
      case (TileDiscarded, DoNothing) => game.nextSeat.setState(NextTurn)

      case _ => game
    }
  }

  implicit class GameActions(game: Game) {

    def seatPlayers(playerNames: Map[Seat, (PlayerType, String)]): Game = {
      val players = Range.inclusive(0, 3).flatMap { seat =>
        playerNames.get(seat) match {
          case None => None
          case Some((playerType, name)) =>
            Some(seat -> Player(
              name,
              0,
              WIND_ORDER(seat),
              playerType,
              Vector(),
              Vector(),
              Vector(),
              Vector(),
            ))
        }
      }.toMap
      game.copy(players=players)
    }

    def dealStartingHands: Game = {
      game
        .players
        .keys
        .foldRight(game) { case (playerSeat, state) =>
          val (tiles, newWall) = game.takeTiles(13)
          state.copy(players=game.addPlayerTiles(playerSeat, tiles), wall=newWall)
        }
        .setState(NextTurn)
    }

    def dealIfMoreTiles: Game = {
      game.takeTiles(1) match {
        case (tile, _) if tile.isEmpty => game.setState(NextRound)
        case (tile, newWall) =>
          game.copy(
            state=TileReceived,
            players=game.addPlayerTiles(game.activeSeat, tile),
            wall=newWall
          )
      }
    }

    def activePlayerDiscards(tileIndex: Int): Game = {
      game.copy(
        state=TileDiscarded,
        players=game.playerDiscards(game.activeSeat, tileIndex),
      )
    }

    def tallyScores: Game = game // TODO: implement

    def nextRound: Game = {
      if (game.prevalentWind == WIND_ORDER.last) game.copy(state=Ended)
      else {
        val tiles = game.wall.dead ++ game.wall.living ++ game.players.flatMap { case (_, p) =>
          p.concealedTiles ++ p.discards
        }

        val shuffled = Random.shuffle(tiles)

        game.copy(
          wall=Wall(shuffled.drop(14), shuffled.take(14)),
          players=game.players.map { case (d, p) => d -> p.copy(
            concealedTiles=Vector(),
            exposedCombinations=Vector(),
            concealedCombinations=Vector(),
            discards=Vector(),
          ) },
          prevalentWind=WIND_ORDER(WIND_ORDER.indexOf(game.prevalentWind) + 1),
          activeSeat=0,
        )
      }
    }
  }

}
