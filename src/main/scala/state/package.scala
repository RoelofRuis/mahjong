import domain.Tile

package object state {

  sealed trait GameState
  case class NewRound() extends GameState
  case class NextTurn() extends GameState
  case class TileReceived() extends GameState
  case class TileDiscarded(tile: Tile) extends GameState
  case class HiddenKongDeclared() extends GameState
  case class OpenPungCompleted() extends GameState
  case class MahjongDeclared() extends GameState
  case class KongDeclared() extends GameState
  case class PungDeclared() extends GameState
  case class ChowDeclared() extends GameState
  case object End extends GameState

}
