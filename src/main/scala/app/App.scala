package app

import app.view.{HTML, View}
import model.Actions.Action
import model.Mahjong.Game
import org.scalajs.dom
import state.{ActionChain, Transitions}

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.Random

@JSExportTopLevel("Mahjong")
object App {

  private var model = Storage.load() match {
    case Right(Some(game)) => game
    case Right(None) => Game(Random)
    case Left(err) =>
      dom.window.alert(err)
      Game(Random)
  }

  def main(args: Array[String]): Unit = render()

  @JSExport("react")
  def react(action: Action): Unit = {
    model = Transitions.react(model, action)
    render()
  }

  private def render(): Unit = {
    ActionChain.getNext(model) match {
      case Some(a) => react(a)
      case None =>
        val (view, draw) = View.render(model)
        HTML.addToPage(view)
        draw(model)
        Storage.save(model)
    }
  }

}
