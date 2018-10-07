package model

import state.NextRound

import scala.util.Random

class NodeInitializer(tiles: Vector[Tile], playerNames: Vector[String]) {
  assert(playerNames.size <= Wind.ORDER.size)

  def initNode(): Node = {
    Node(
      NextRound,
      Wind.ORDER(0),
      initWall(),
      initPlayers(),
      0
    )
  }

  def initWall(): Wall = {
    val shuffled = Random.shuffle(tiles)

    Wall(
      shuffled.drop(14),
      shuffled.take(14)
    )
  }

  def initPlayers(): Map[Int, Player] = {
    playerNames.zipWithIndex.map { case (name, index) =>
      index -> Player(
        name,
        0,
        Wind.ORDER(index),
        Hand(),
        Discards()
      )
    }.toMap
  }

}
