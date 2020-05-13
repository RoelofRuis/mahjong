package model

import scala.collection.immutable.{Map, Vector}

object Mahjong {

  sealed trait WindDirection
  final case object North extends WindDirection
  final case object South extends WindDirection
  final case  object East extends WindDirection
  final case object West extends WindDirection

  sealed trait DragonColor
  final case object Red extends DragonColor
  final case object White extends DragonColor
  final case object Green extends DragonColor

  sealed trait Suite
  final case object Bamboos extends Suite
  final case object Circles extends Suite
  final case object Characters extends Suite

  sealed trait Tile
  final case class DragonTile(color: DragonColor) extends Tile
  final case class WindTile(windDirection: WindDirection) extends Tile
  final case class SuitedTile(suite: Suite, number: Int) extends Tile

  trait Combination
  final case class Pung(tile: Tile) extends Combination
  final case class Kong(tile: Tile) extends Combination
  final case class Chow(tile: Tile) extends Combination

  final val WIND_ORDER: Vector[WindDirection] = Vector[WindDirection](East, South, West, North)

  final case class Hand(
    concealedTiles: Vector[Tile] = Vector[Tile](),
    exposedCombinations: Vector[Combination] = Vector[Combination](),
    concealedCombinations: Vector[Combination] = Vector[Combination]()
  )

  final case class Discards(
    discards: Vector[Tile] = Vector[Tile]()
  )

  final case class Player(
    name: String,
    score: Int,
    wind: WindDirection,
    hand: Hand = Hand(),
    discards: Discards = Discards(),
  )

  final case class Wall(
    living: Vector[Tile],
    dead: Vector[Tile]
  )

  final case class Round(
    wind: WindDirection,
    turn: Int,
    activePlayer: WindDirection,
  )

  final case class Game(
    round: Round,
    wall: Wall,
    players: Map[WindDirection, Player]
  )

}
