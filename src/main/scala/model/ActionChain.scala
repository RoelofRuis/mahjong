package model

import model.Actions.{Action, DealTile, TallyScores}
import model.Mahjong._

object ActionChain {

  def getNext(model: Game): Option[Action] = {
    model.state match {
      case NextTurn => Some(DealTile)
      case NextRound => Some(TallyScores)
      case _ => None
    }
  }

}
