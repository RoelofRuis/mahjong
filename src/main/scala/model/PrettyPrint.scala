package model

object PrettyPrint {

  trait PrettyPrinter[A] {
    def format(a: A): String
  }

  implicit object PrettyHand extends PrettyPrinter[Hand] {
    def format(hand: Hand): String = {
      "c: [%s]\nC: [%s]\nE: [%s]" format (
        hand.concealedTiles.mkString(", "),
        hand.concealedCombinations.mkString(", "),
        hand.exposedCombinations.mkString(", ")
      )
    }
  }

  implicit object PrettyPlayer extends PrettyPrinter[Player] {
    def format(player: Player): String = {
      "%s: <%s> - %d\n%s" format (player.wind, player.name, player.score, prettyPrint(player.hand))
    }
  }

  implicit object PrettyWall extends PrettyPrinter[Wall] {
    def format(wall: Wall): String = {
      "Wall: %d - %d" format (wall.living.size, wall.dead.size)
    }
  }

  implicit object PrettyNode extends PrettyPrinter[Node] {
    def format(node: Node): String = {
      "Round: %s\n%s\nActive Player: %s\n\n%s\n" format (
        node.windOfRound,
        prettyPrint(node.wall),
        node.activePlayer,
        node.players.values.map((player: Player) => prettyPrint(player)).mkString("\n\n")
      )
    }
  }

  def prettyPrint[A](obj: A)(implicit p: PrettyPrinter[A]): String = p.format(obj)

}
