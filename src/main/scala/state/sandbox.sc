import model.NodeInitializer
import state.GameLoop

object sandbox {

  val bamboos = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => s"bamboo $i")
  val circles = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => s"circles $i")
  val characters = (1 to 9).flatMap(Vector.fill(4)(_)).map(i => s"characters $i")
  val dragons = Vector("red", "green", "white").flatMap(Vector.fill(4)(_))
  val winds = Vector("east", "west", "north", "south").flatMap(Vector.fill(4)(_))

  val Tiles = (bamboos ++ circles ++ characters ++ dragons ++ winds).toVector

  val Players = Vector("alice", "bob", "charlie", "dave")

  val initializer = new NodeInitializer(Tiles, Players)

  println(initializer.initNode())

  val gameLoop = GameLoop(initializer.initNode())

  val finalState = gameLoop.run()

  println(finalState.players(finalState.activePlayer).hand)

}