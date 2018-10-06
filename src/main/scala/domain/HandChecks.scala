package domain

trait HandChecks {
  protected def getHand: Hand

  def validChow(pos0: Int, pos1: Int, ext: Tile): Boolean = ???
  def validPung(pos0: Int, pos1: Int, ext: Tile): Boolean = ???
  def validBigMeldedKong(pos0: Int, pos1: Int, pos2: Int, ext: Tile): Boolean = ???
  def validSmallMeldedKong(combinationPos: Int, ext: Tile): Boolean = ???
  def validConcealedKong(pos0: Int, pos1: Int, pos2: Int, pos3: Int): Boolean = ???
  def validMahjong(): Boolean = ???

  def tileByPos(pos: Int): Option[Tile] = ???

}
