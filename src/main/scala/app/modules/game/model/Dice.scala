package app.modules.game.model

import scala.util.Random

object Dice {

  def roll: Int = (Random.nextInt(6) + 1) + (Random.nextInt(6) + 1)

}
