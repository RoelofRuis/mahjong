package app

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, html}
import scalatags.JsDom

object HTML {

  def addToPage(content: Map[String, JsDom.TypedTag[HTMLElement]]): Unit = {
    content.foreach { case (rootId, content) =>
      val rootNode = document.getElementById(rootId)
      rootNode.innerHTML = ""
      rootNode.appendChild(content.render)
    }
  }

  def updateLabel(id: String, value: String): Unit = {
    Option(document.getElementById(id).asInstanceOf[html.Input]).foreach(_.textContent = value)
  }

  def inputValue(id: String): Option[String] = {
    Option(document.getElementById(id).asInstanceOf[html.Input].value).filter(_.trim.nonEmpty)
  }

}
