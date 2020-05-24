package model

import model.Actions.{Discard, ReactToReceive}
import model.Mahjong.{DiscardReaction, DoNothing, Player}

object PlayerActions {

  implicit class PlayerActionOps(player: Player) {

    def validActionsOnReceive: Seq[ReactToReceive] = {
      player.concealedTiles.map(Discard)
    }

    def validActionsOnDiscard: Seq[DiscardReaction] = Seq(DoNothing)

  }

}
