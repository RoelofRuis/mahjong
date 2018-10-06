import domain.Tile

package object state {

  sealed trait GameState

  case class NewGame() extends GameState
  case class NewRound() extends GameState
  case class NextTurn() extends GameState
  case class TileReceived() extends GameState
  case class TileDiscarded(tile: Tile) extends GameState
  case class KongDeclared() extends GameState
  case class MahjongDeclared() extends GameState

  case object Start extends GameState
  case object End extends GameState

}
