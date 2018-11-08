package model

import scala.collection.immutable.Map

case class Node(
  windOfRound: Wind,
  wall: Wall,
  players: Map[Wind, Player],
  activePlayer: Wind
) {

  def setWall(wall: Wall): Node = this.copy(wall = wall)

  def addPlayerConcealedTiles(wind: Wind, tiles: Vector[Tile]): Node = {
    players.get(wind).map { player =>
      player.copy(
        hand = player.hand.copy(
          concealedTiles = player.hand.concealedTiles ++ tiles
        )
      )
    }.map { player =>
      this.copy(
        players = players.updated(wind, player)
      )
    }.getOrElse(this)
  }

}

