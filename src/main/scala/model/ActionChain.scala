package model

import model.Actions.{Action, DealTile, Discard, TallyScores}
import model.Mahjong._

object ActionChain {

  import MahjongOps._

  def getNext(model: Game): Option[Action] = {
    model.state match {
      case NextTurn => Some(DealTile)
      case NextRound => Some(TallyScores)
      case TileReceived if model.activePlayerIsAI =>
        // TODO: actual AI move
        Some(Discard(0))

      case _ => None
    }
  }

}
