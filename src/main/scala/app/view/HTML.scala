package app.view

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, html}
import scalatags.JsDom

object HTML {

  def addToPage(content: JsDom.TypedTag[HTMLElement], rootId: String = "root"): Unit = {
    val rootNode = document.getElementById(rootId)
    rootNode.innerHTML = ""
    rootNode.appendChild(content.render)
  }

  def inputValue(id: String): Option[String] = {
    Option(document.getElementById(id).asInstanceOf[html.Input].value).filter(_.trim.nonEmpty)
  }

}
