package app.modules.game.model

import app.modules.game.model.Mahjong.{DiscardReaction, PlayerType, Seat, Tile}

object Actions {

  sealed trait Action

  case class NewGame(players: Map[Seat, (PlayerType, String)]) extends Action
  case object Restart extends Action
  case object DealTile extends Action
  case object TallyScores extends Action

  // Player actions
  sealed trait ReactToReceive extends Action
  case class Discard(tile: Tile) extends ReactToReceive
  case class DeclareConcealedKong(tilePos0: Int, tilePos1: Int, tilePos2: Int, tilePos3: Int) extends ReactToReceive
  case class DeclareSmallMeldedKong(combinationPos: Int, tilePos: Int) extends ReactToReceive
  case object DeclareMahjong extends ReactToReceive
  case class ReactToDiscard(seat: Seat, reaction: DiscardReaction) extends Action

}
