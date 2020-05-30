package app.modules.game.model

import app.modules.game.model.Actions._
import app.modules.game.model.Mahjong._

import scala.util.Random

object Transitions {

  import MahjongOps._

  def react(game: Table, action: Action): Table = {
    (game.state, action) match {
      case (_, Restart) =>
        Mahjong.newGame(Random)

      case (Uninitialized, NewGame(playerData)) =>
        game
          .seatPlayers(playerData)
          .dealStartingHands
          .setState(NextTurn)

      case (NextTurn, DealTile) =>
        game
          .dealIfMoreTiles

      case (TileReceived, Discard(t)) =>
        game
          .activePlayerDiscards(t)
          .setState(TileDiscarded())

      case (TileDiscarded(map), ReactToDiscard(seat, reaction)) =>
        game
          .setState(TileDiscarded(map.updated(seat, reaction)))
          .determineDiscardResult

      case (NextRound, TallyScores) =>
        if (game.prevalentWind == WIND_ORDER.last) game.tallyScores.setState(Ended)
        else game.tallyScores.nextRound.dealStartingHands.setState(NextTurn)

      case _ => game
    }
  }

  implicit class GameActions(game: Table) {

    def seatPlayers(playerNames: Map[Seat, (PlayerType, String)]): Table = {
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

    def dealStartingHands: Table = {
      game
        .players
        .keys
        .foldRight(game) { case (playerSeat, state) =>
          val (tiles, newWall) = state.takeTiles(13)
          state.copy(
            players=state.addPlayerTiles(playerSeat, tiles),
            wall=newWall
          )
        }
    }

    def dealIfMoreTiles: Table = {
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

    def activePlayerDiscards(tile: Tile): Table = {
      game.copy(
        players=game.playerDiscards(game.activeSeat, tile),
      )
    }

    def determineDiscardResult: Table = {
      game.state match {
        case TileDiscarded(reactions) =>
          if (reactions.size < (game.players.size - 1)) game
          else {
            // TODO: implement
            game
              .nextSeat
              .setState(NextTurn)
          }
        case _ => game
      }
    }

    def tallyScores: Table = game // TODO: implement

    def nextRound: Table = {
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
