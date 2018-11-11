package model

import scala.collection.immutable.Map

case class Table(
  round: Round,
  wall: Wall,
  players: Map[Wind, Player]
) {

  def setWall(wall: Wall): Table = this.copy(wall = wall)

  def addPlayerConcealedTiles(wind: Wind, tiles: Vector[Tile]): Table = {
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

