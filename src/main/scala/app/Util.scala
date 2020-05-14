package app

import org.scalajs.dom

object Util {

  def printDebug(obj: Any): Unit = {
    val debug = dom.document.getElementById("debug")
    debug.innerHTML = obj.toString
  }

}
