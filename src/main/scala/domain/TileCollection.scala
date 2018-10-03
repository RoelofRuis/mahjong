package domain

import scala.collection.mutable.ListBuffer
import scala.util.Random

class TileCollection(tiles: Iterable[Tile] = Seq()) {
  private val s: ListBuffer[Tile] = new ListBuffer[Tile]

  s.appendAll(tiles)

  def insert(other: TileCollection): Unit = s.appendAll(other.iterate())
  def add(t: Tile, n: Int): Unit = (1 to n).foreach(_ => s.append(t))
  def take(n: Int): TileCollection = new TileCollection((1 to n).map(_ => s.remove(0)))
  def size(): Int = s.size
  def shuffled(): TileCollection = new TileCollection(Random.shuffle(s))
  def describe(): String = s.mkString(", ")

  protected def iterate(): Iterable[Tile] = s
}
