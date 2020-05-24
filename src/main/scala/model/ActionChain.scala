package model

import model.Actions._
import model.Mahjong._

object ActionChain {

  import MahjongOps._

  def getNext(game: Game): Option[Action] = {
    game.state match {
      case NextTurn => Some(DealTile)
      case NextRound => Some(TallyScores)
      case TileReceived if game.activePlayerIsAI =>
        // TODO: actual AI move
        Some(Discard(game.activePlayer.get.concealedTiles.head))

      case TileDiscarded(res) =>
        game.getAIPlayerSeats.toSet.diff(res.keys.toSet).toList match {
          case List() => None
          case seat :: _ =>
            // TODO: actual AI move
            Some(ReactToDiscard(seat, DoNothing))
        }

      case _ => None
    }
  }

}
