package app.modules.game.model

import app.modules.game.model.Actions.{Discard, ReactToReceive}
import app.modules.game.model.Mahjong.{DiscardReaction, DoNothing, Table, Player, Seat, Tile}

object PlayerActions {

  import app.modules.game.model.MahjongOps._

  implicit class PlayerActionOps(game: Table) {

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
