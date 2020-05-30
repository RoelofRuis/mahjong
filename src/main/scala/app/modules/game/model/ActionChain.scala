package app.modules.game.model

import app.modules.game.model.Actions._
import app.modules.game.model.Mahjong._

object ActionChain {

  import MahjongOps._

  def getNext(game: Table): Option[Action] = {
    game.state match {
      case NextTurn => Some(DealTile)
      case NextRound => Some(TallyScores)
      case TileReceived if game.activePlayerIsAI =>
        // TODO: actual AI move
        Some(Discard(game.activePlayer.get.concealedTiles.head))

      case TileDiscarded(res) =>
        (game.getAIPlayerSeats.toSet - game.activeSeat).diff(res.keys.toSet).toList match {
          case List() => None
          case seat :: _ =>
            // TODO: actual AI move
            Some(ReactToDiscard(seat, DoNothing))
        }

      case _ => None
    }
  }

}
