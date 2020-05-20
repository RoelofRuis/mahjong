package state

import model.Actions.{Action, DealTile}
import model.Mahjong._

object ActionChain {

  def getNext(model: Game): Option[Action] = {
    model.state match {
      case NextTurn => Some(DealTile)
      case _ => None
    }
  }

}
