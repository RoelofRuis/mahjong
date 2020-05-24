package model

import model.Actions.{Discard, ReactToReceive}
import model.Mahjong.{DiscardReaction, DoNothing, Game, Player, Seat, Tile}

object PlayerActions {

  import model.MahjongOps._

  implicit class PlayerActionOps(game: Game) {

    def validActionsOnReceive: Seq[ReactToReceive] = {
      game.activePlayer.map { player =>
        player.concealedTiles.map(Discard)
      }.getOrElse(Seq())
    }

    def validActionsOnDiscard(seat: Seat): Seq[DiscardReaction] = {
      val reaction: Option[(Player, Player, Tile)] = for {
        (discardingPlayer, tile) <- game.lastDiscard
        reactingPlayer <- game.players.get(seat)
        if discardingPlayer != reactingPlayer
      } yield (reactingPlayer, discardingPlayer, tile)

      reaction.map { case (reactingPlayer, discardingPlayer, tile) =>
        Seq(DoNothing)
      }.getOrElse(Seq())
    }

  }

}
