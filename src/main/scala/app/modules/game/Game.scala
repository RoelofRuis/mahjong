package app.modules.game

import app.modules.game.model.Actions.Action
import app.modules.game.model.{ActionChain, Mahjong, Transitions}
import app.{HTML, Storage}
import org.scalajs.dom

import scala.util.Random

object Game {

  private var model = Storage.load() match {
    case Right(Some(game)) => game
    case Right(None) => Mahjong.newGame(Random)
    case Left(err) =>
      dom.window.alert(err)
      Mahjong.newGame(Random)
  }

  def react(action: Action): Unit = {
    model = Transitions.react(model, action)
    view()
  }

  def view(): Unit = {
    ActionChain.getNext(model) match {
      case Some(a) => react(a)
      case None =>
        val (view, draw) = View.render(model)
        HTML.addToPage(view)
        draw()
        Storage.save(model)
    }
  }

}
