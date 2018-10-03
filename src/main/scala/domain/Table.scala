package domain

import scala.collection.mutable

class Table(players: Iterable[Player]) {
  private val seats: mutable.Queue[Seat] = mutable.Queue[Seat]()

  players.zipWithIndex.foreach { case (player, index) => seats.enqueue(new Seat(player, index)) }

  def getAllPlayers: Iterable[Player] = seats.map(_.getPlayer)

  def getActivePlayer: Player = seats.head.getPlayer

  def getNonActiveSeats: Iterable[Seat] = seats.tail

  def makeNextSeatActive(): Unit = {
    val temp = seats.dequeue()
    seats.enqueue(temp)
  }

  def makeSeatActive(seat: Seat): Unit = {
    val temp = mutable.Queue[Seat]()

    while (seats.head != seat) {
      temp.enqueue(seats.dequeue)
    }

    temp.dequeueAll(_ => true).foreach(seats.enqueue(_))
  }
}