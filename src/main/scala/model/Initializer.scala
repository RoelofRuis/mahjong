package model

import scala.util.Random

object Initializer {

  def tileset: Vector[Tile] = {
    val bamboos = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => s"b$i")
    val circles = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => s"c$i")
    val characters = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => s"h$i")
    val dragons = Vector("dr", "dg", "dw").flatMap(Vector.fill(4)(_))
    val winds = Vector("we", "ww", "wn", "ws").flatMap(Vector.fill(4)(_))

    (bamboos ++ circles ++ characters ++ dragons ++ winds).toVector
  }

  def newGame(tiles: Vector[Tile], playerNames: Vector[String], random: Random): Node = {
    assert(playerNames.size <= Wind.ORDER.size)

    val shuffled = random.shuffle(tiles)

    val wall = Wall(
      shuffled.drop(14),
      shuffled.take(14)
    )

    val players = playerNames.zipWithIndex.map { case (name, index) =>
      Wind.ORDER(index) -> Player(
        name,
        0,
        Wind.ORDER(index),
        Hand(),
        Discards()
      )
    }.toMap

    Node(
      Wind.ORDER(0),
      wall,
      players,
      Wind.ORDER(0)
    )
  }
}
