package app.modules.game.model

import upickle.default.{macroRW, ReadWriter => RW}

import scala.collection.immutable.{Map, Vector}
import scala.util.Random

object Mahjong {

  sealed trait WindDirection
  final case object North extends WindDirection
  final case object South extends WindDirection
  final case object East extends WindDirection
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

  sealed trait Combination
  final case class Pung(tile: Tile) extends Combination
  final case class Kong(tile: Tile) extends Combination
  final case class Chow(tile: Tile) extends Combination

  final val WIND_ORDER: Vector[WindDirection] = Vector[WindDirection](East, South, West, North)

  lazy val TILESET: Vector[Tile] = {
    val bamboos = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => SuitedTile(Bamboos, i))
    val circles = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => SuitedTile(Circles, i))
    val characters = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => SuitedTile(Characters, i))
    val dragons = Vector(Red, White, Green).flatMap(Vector.fill(4)(_)).map(c => DragonTile(c))
    val winds = Vector(North, South, East, West).flatMap(Vector.fill(4)(_)).map(d => WindTile(d))

    (bamboos ++ circles ++ characters ++ dragons ++ winds).toVector
  }

  sealed trait DiscardReaction
  final case class DeclarePung(tile0: Int, tile1: Int, discard: Int) extends DiscardReaction
  final case class DeclareChow(tile0: Int, tile1: Int, discard: Int) extends DiscardReaction
  final case class DeclareBigMeldedKong(tile0: Int, tile1: Int, tile2: Int) extends DiscardReaction
  final case object DoNothing extends DiscardReaction
  final case object DeclareMahjong extends DiscardReaction

  type Seat = Int

  type Players = Map[Seat, Player]

  sealed trait PlayerType
  final case object ComputerControlled extends PlayerType
  final case object HumanControlled extends PlayerType

  final case class Player(
    name: String,
    score: Int,
    seatWind: WindDirection,
    playerType: PlayerType,
    concealedTiles: Vector[Tile] = Vector[Tile](),
    exposedCombinations: Vector[Combination] = Vector[Combination](),
    concealedCombinations: Vector[Combination] = Vector[Combination](),
    discards: Vector[Tile] = Vector[Tile]()
  )

  final case class Wall(
    living: Vector[Tile],
    dead: Vector[Tile]
  )

  sealed trait State
  final case object Uninitialized extends State
  final case object NextRound extends State
  final case object NextTurn extends State
  final case object TileReceived extends State
  final case class TileDiscarded(reactions: Map[Seat, DiscardReaction] = Map()) extends State
  final case object KongDeclared extends State
  final case object MahjongDeclared extends State
  final case object Ended extends State

  final case class Table(
    state: State,
    wall: Wall,
    players: Players,
    prevalentWind: WindDirection,
    activeSeat: Seat,
  )

  def newGame(random: Random): Table = {
    val shuffled = random.shuffle(TILESET)

    Table(
      Uninitialized,
      Wall(shuffled.drop(14), shuffled.take(14)),
      Map(),
      WIND_ORDER(0),
      0
    )
  }

  object Serializable {
    implicit val rwWindDirection: RW[WindDirection] = macroRW
    implicit val rwDragonColor: RW[DragonColor] = macroRW
    implicit val rwSuite: RW[Suite] = macroRW
    implicit val rwDragonTile: RW[DragonTile] = macroRW
    implicit val rwWindTile: RW[WindTile] = macroRW
    implicit val rwSuitedTile: RW[SuitedTile] = macroRW
    implicit val rwTile: RW[Tile] = macroRW
    implicit val rwPung: RW[Pung] = macroRW
    implicit val rwKong: RW[Kong] = macroRW
    implicit val rwChow: RW[Chow] = macroRW
    implicit val rwCombination: RW[Combination] = macroRW
    implicit val rwPlayerType: RW[PlayerType] = macroRW
    implicit val rwPlayer: RW[Player] = macroRW
    implicit val rwWall: RW[Wall] = macroRW
    implicit val rwDeclarePung: RW[DeclarePung] = macroRW
    implicit val rwDeclareChow: RW[DeclareChow] = macroRW
    implicit val rwDeclareBigMeldedKong: RW[DeclareBigMeldedKong] = macroRW
    implicit val rwDiscardReaction: RW[DiscardReaction] = macroRW
    implicit val rwTileDiscarded: RW[TileDiscarded] = macroRW
    implicit val rwState: RW[State] = macroRW
    implicit val rwGame: RW[Table] = macroRW
  }

}
