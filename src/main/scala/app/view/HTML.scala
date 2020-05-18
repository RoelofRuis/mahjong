package app.view

import org.scalajs.dom.html.Div
import org.scalajs.dom.{document, html}
import scalatags.JsDom

object HTML {

  def addToPage(content: JsDom.TypedTag[Div], rootId: String = "root"): Unit = {
    val rootNode = document.getElementById(rootId)
    rootNode.innerHTML = ""
    rootNode.appendChild(content.render)
  }

  def inputValue(id: String): Option[String] = {
    Option(document.getElementById(id).asInstanceOf[html.Input].value).filter(_.trim.nonEmpty)
  }

}
