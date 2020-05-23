package model

import model.Mahjong.{DiscardReaction, PlayerType, Seat}

object Actions {

  sealed trait Action

  case class NewGame(players: Map[Seat, (PlayerType, String)]) extends Action
  case object Restart extends Action
  case object DealTile extends Action
  case object TallyScores extends Action

  // Player actions
  case class Discard(tilePos: Int) extends Action
  case class DeclareConcealedKong(tilePos0: Int, tilePos1: Int, tilePos2: Int, tilePos3: Int) extends Action
  case class DeclareSmallMeldedKong(combinationPos: Int, tilePos: Int) extends Action
  case object DeclareMahjong extends Action
  case class ReactToDiscard(seat: Seat, reaction: DiscardReaction) extends Action

}
