package app

import model.Mahjong.Game
import org.scalajs.dom
import upickle.default._

import scala.util.{Failure, Success, Try}

object Storage {

  import model.Mahjong.Serializable._

  private val GAME_KEY = "GAME"

  def save(game: Game): Unit = {
    val string = write(game)
    dom.window.localStorage.setItem(GAME_KEY, string)
  }

  def load(): Either[String, Option[Game]] = {
    Option(dom.window.localStorage.getItem(GAME_KEY)) match {
      case None => Right(None)
      case Some(string) =>
        Try(read[Game](string)) match {
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
