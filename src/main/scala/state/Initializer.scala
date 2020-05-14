package state

import model.Mahjong._

import scala.util.Random

object Initializer {

  lazy val tileset: Vector[Tile] = {
    val bamboos = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => SuitedTile(Bamboos, i))
    val circles = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => SuitedTile(Circles, i))
    val characters = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => SuitedTile(Characters, i))
    val dragons = Vector(Red, White, Green).flatMap(Vector.fill(4)(_)).map(c => DragonTile(c))
    val winds = Vector(North, South, East, West).flatMap(Vector.fill(4)(_)).map(d => WindTile(d))

    (bamboos ++ circles ++ characters ++ dragons ++ winds).toVector
  }

  def newGame(playerNames: Vector[String], random: Random): Game = {
    assert(playerNames.size <= WIND_ORDER.size)

    val shuffled = random.shuffle(tileset)

    val wall = Wall(
      shuffled.drop(14),
      shuffled.take(14)
    )

    val players = playerNames.zipWithIndex.map { case (name, index) =>
      WIND_ORDER(index) -> Player(
        name,
        0,
        WIND_ORDER(index),
        Hand(),
        Discards()
      )
    }.toMap

    val round = Round(
      WIND_ORDER(0),
      0,
      WIND_ORDER(0)
    )

    Game(round, wall, players)
  }
}
