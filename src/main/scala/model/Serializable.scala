package model

import model.Mahjong.{Chow, Combination, Discards, DragonColor, DragonTile, Game, Hand, Kong, Player, Pung, Round, Suite, SuitedTile, Tile, Wall, WindDirection, WindTile}
import upickle.default.{ReadWriter => RW, macroRW}

object Serializable {
  implicit val rwWindDirection: RW[WindDirection] = macroRW
  implicit val rwDragonColor: RW[DragonColor] = macroRW
  implicit val rwSuite: RW[Suite] = macroRW
  implicit val rwDragonTile: RW[DragonTile] = macroRW
  implicit val rwWindTile: RW[WindTile] = macroRW
  implicit val rwSuitedTile: RW[SuitedTile] = macroRW
  implicit val rwTile: RW[Tile] = macroRW
  implicit val rwPung: RW[Pung] = macroRW
  implicit val rwKong: RW[Kong] = macroRW
  implicit val rwChow: RW[Chow] = macroRW
  implicit val rwCombination: RW[Combination] = macroRW
  implicit val rwHand: RW[Hand] = macroRW
  implicit val rwDiscards: RW[Discards] = macroRW
  implicit val rwPlayer: RW[Player] = macroRW
  implicit val rwWall: RW[Wall] = macroRW
  implicit val rwRound: RW[Round] = macroRW
  implicit val rwGame: RW[Game] = macroRW
}
