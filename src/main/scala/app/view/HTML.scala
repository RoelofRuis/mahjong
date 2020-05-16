package app.view

import org.scalajs.dom.{document, html}
import scalatags.Text

object HTML {

  def render(content: Text.TypedTag[String], rootId: String = "root") = {
    document.getElementById("root").innerHTML = content.render
  }

  def inputValue(id: String): Option[String] = {
    Option(document.getElementById(id).asInstanceOf[html.Input].value).filter(_.trim.nonEmpty)
  }

}
