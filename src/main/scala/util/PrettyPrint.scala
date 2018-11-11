package util

import model._

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

  implicit object PrettyRound extends PrettyPrinter[Round] {
    def format(round: Round): String = {
      "Round: %s\nTurn: %d\nActive Player: %s" format (
        round.wind,
        round.turn,
        round.activePlayer
      )
    }
  }

  implicit object PrettyNode extends PrettyPrinter[Table] {
    def format(node: Table): String = {
      "%s\n\n%s\n\n%s" format (
        prettyPrint(node.round),
        prettyPrint(node.wall),
        node.players.values.map((player: Player) => prettyPrint(player)).mkString("\n\n")
      )
    }
  }

  def prettyPrint[A](obj: A)(implicit p: PrettyPrinter[A]): String = p.format(obj)

}
