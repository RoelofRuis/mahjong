package app

import app.modules.game.model.Mahjong.Table
import org.scalajs.dom
import upickle.default._

import scala.util.{Failure, Success, Try}

object Storage {

  import app.modules.game.model.Mahjong.Serializable._

  private val GAME_KEY = "GAME"

  def save(game: Table): Unit = {
    val string = write(game)
    dom.window.localStorage.setItem(GAME_KEY, string)
  }

  def load(): Either[String, Option[Table]] = {
    Option(dom.window.localStorage.getItem(GAME_KEY)) match {
      case None => Right(None)
      case Some(string) =>
        Try(read[Table](string)) match {
          case Success(game) => Right(Some(game))
          case Failure(_) =>
            remove()
            Left("Unable to load game: Corrupt game state")
        }
    }
  }

  def remove(): Unit = {
    dom.window.localStorage.removeItem(GAME_KEY)
  }

}
