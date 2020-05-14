package model

import model.Mahjong.{DragonTile, Game, SuitedTile, Tile, WindTile}

object Debugger {

  implicit class GameDebugger(game: Game) {

    def prettyPrint: String = {
      val round = game.round
      val roundString = s"Round of ${round.wind} - Player ${round.activePlayer} (Turn ${round.turn})"
      val wall = game.wall
      val livingWallString = wall.living.map(printTile).mkString(", ")
      val deadWallString = wall.dead.map(printTile).mkString(", ")
      val wallString = s"$livingWallString\n$deadWallString"
      s"$roundString\n$wallString"
    }

    private def printTile(tile: Tile): String = {
        tile match {
          case DragonTile(c) => s"$c Dragon"
          case WindTile(d) => s"$d Wind"
          case SuitedTile(s, n) => s"$n of $s"
        }
    }

  }

}
