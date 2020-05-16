package app

import model.Mahjong.Game
import org.scalajs.dom
import upickle.default._

object Storage {

  import model.Mahjong.Serializable._

  private val GAME_KEY = "GAME"

  def saveGame(game: Game): Unit = {
    val string = write(game)
    dom.window.localStorage.setItem(GAME_KEY, string)
  }

  def loadGame(): Option[Game] = {
    Option(dom.window.localStorage.getItem(GAME_KEY)) match {
      case Some(string) => Some(read[Game](string))
      case None => None
    }
  }

}
