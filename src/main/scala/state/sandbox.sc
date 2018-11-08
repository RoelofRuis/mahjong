import model.{Initializer, PrettyPrint}
import state.{GameLoop, NextRound}

import scala.util.Random

object sandbox {

  val Players = Vector("alice", "bob", "charlie", "dave")

  val Tiles = Initializer.tileset

  val game = Initializer.newGame(Tiles, Players, Random)

  val finalState = GameLoop.run(game, NextRound.transition)

  print(PrettyPrint.prettyPrint(finalState))

}