package model

case class Player(
  name: String,
  score: Int,
  wind: Wind,
  hand: Hand = Hand(),
  discards: Discards = Discards(),
)
