import domain._
import state._
import state.transitions.{NewRoundTransition, NextTurnTransition, TileDiscardedTransition, TileReceivedTransition}

object sandbox {

  val Tiles: TileCollection = new TileCollection()
  (1 to 9).foreach(i => Tiles.add(s"bamboo $i", 4))
  (1 to 9).foreach(i => Tiles.add(s"circles $i", 4))
  (1 to 9).foreach(i => Tiles.add(s"characters $i", 4))
  Seq("red", "green", "white").foreach(c => Tiles.add(s"$c dragon", 4))
  Seq("east", "west", "north", "south").foreach(d => Tiles.add(s"$d wind", 4))

  val WallBuilder = new WallBuilder(Tiles)

  val Dealer = new Dealer(WallBuilder)

  val Player1: Player = new Player("player 1")
  val Player2: Player = new Player("player 2")
  val Player3: Player = new Player("player 3")
  val Player4: Player = new Player("player 4")

  val Table = new Table(Seq(Player1, Player2, Player3, Player4))

  val NewRoundTransition = new NewRoundTransition(Dealer)
  val NextTurnTransition = new NextTurnTransition(Dealer)
  val TileDiscardedTransition = new TileDiscardedTransition
  val TileReceivedTransition = new TileReceivedTransition

  val transitions: (Table, GameState) => GameState = { (table, current) =>
    current match {
      case s: NewRound => NewRoundTransition.next(s, table)
      case s: NextTurn => NextTurnTransition.next(s, table)
      case s: TileDiscarded => TileDiscardedTransition.next(s, table)
      case s: TileReceived => TileReceivedTransition.next(s, table)
      case s =>
        println(s"received unknown state: $s")
        End
    }

  }

  val gameLoop = new GameLoop(Table, NewRound(), transitions)

  gameLoop.run()

}