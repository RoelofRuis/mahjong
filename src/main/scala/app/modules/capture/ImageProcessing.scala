package app.modules.capture

import org.scalajs.dom.ImageData

object ImageProcessing {

  implicit class ImageDataOps(source: ImageData) {
    val width: Int = source.width
    val height: Int = source.height
    val numPix: Int = source.data.length / 4
    assert(source.data.length % 4 == 0)

    @inline
    def r(x: Int, y: Int): Double = source.data(((y * width) + x) * 4)
    @inline
    def g(x: Int, y: Int): Double = source.data((((y * width) + x) * 4) + 1)
    @inline
    def b(x: Int, y: Int): Double = source.data((((y * width) + x) * 4) + 2)

    @inline
    def putPixel(x: Int, y: Int, r: Double, g: Double, b: Double): Unit = {
      val i = ((y * width) + x) * 4
      source.data(i) = r.toInt
      source.data(i + 1) = g.toInt
      source.data(i + 2) = b.toInt
    }

    def grayscale(): Unit = {
      for (y: Int <- 0 until height) {
        for (x: Int <- 0 until width) {
          val avg = ((r(x,y) + g(x,y) + b(x,y)) / 3).toInt
          putPixel(x, y, avg, avg, avg)
        }
      }
    }

    def blur(): Unit = {
      for (y: Int <- 2 until height - 2) {
        for (x: Int <- 2 until width - 2) {
          putPixel(x, y,
            (r(x - 1, y) + r(x, y) * 2 + r(x + 1, y)) / 4,
            (g(x - 1, y) + g(x, y) * 2 + g(x + 1, y)) / 4,
            (b(x - 1, y) + b(x, y) * 2 + b(x + 1, y)) / 4,
          )
        }
      }
    }

    def threshold(t: Double): Unit = {
      for (y: Int <- 0 until height) {
        for (x: Int <- 0 until width) {
          if (r(x, y) > t) putPixel(x, y, 255D, 255D, 255D)
        }
      }
    }
  }

}
