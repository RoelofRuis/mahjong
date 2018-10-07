package model

import state.GameState

import scala.collection.immutable.Map

case class Node(
  state: GameState,
  windOfRound: Wind,
  wall: Wall,
  players: Map[Int, Player],
  activePlayer: Int
)
