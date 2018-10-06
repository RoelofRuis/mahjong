package domain

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class TileCollection(tiles: Iterable[Tile] = Seq()) {
  private val s: ArrayBuffer[Tile] = new ArrayBuffer[Tile]

  s.appendAll(tiles)

  def insert(other: TileCollection): Unit = s.appendAll(other.iterate())
  def add(t: Tile, n: Int): Unit = (1 to n).foreach(_ => s.append(t))
  def take(n: Int): TileCollection = new TileCollection((1 to n).map(_ => s.remove(0)))
  def size(): Int = s.size
  def shuffled(): TileCollection = new TileCollection(Random.shuffle(s))
  def toArray: Array[Tile] = s.toArray

  protected def iterate(): Iterable[Tile] = s
}
