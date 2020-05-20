package model

import model.Mahjong.WindDirection

object Actions {

  sealed trait Action

  case class NewGame(players: Map[WindDirection, String]) extends Action
  case object Restart extends Action
  case object DealTile extends Action
  case object TallyScores extends Action

  case class Discard(tilePos: Int) extends Action
  case class DeclareConcealedKong(tilePos0: Int, tilePos1: Int, tilePos2: Int, tilePos3: Int) extends Action
  case class DeclareSmallMeldedKong(combinationPos: Int, tilePos: Int) extends Action
  case object DeclareMahjong extends Action

  case class DeclarePung(tile0: Int, tile1: Int, discard: Int) extends Action
  case class DeclareChow(tile0: Int, tile1: Int, discard: Int) extends Action
  case class DeclareBigMeldedKong(tile0: Int, tile1: Int, tile2: Int) extends Action
  case object DoNothing extends Action

}
