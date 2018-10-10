package model

import scalaz.{Lens, LensFamily}

case class Player(
  name: String,
  score: Int,
  wind: Wind,
  hand: Hand = Hand(),
  discards: Discards = Discards(),
)

object Player {

  val hand: Lens[Player, Hand] = LensFamily.lensu[Player, Hand](
    (player, hand) => player.copy(hand = hand),
    _.hand
  )

}