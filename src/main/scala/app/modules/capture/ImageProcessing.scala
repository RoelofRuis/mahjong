package app.modules.capture

import typings.std.ImageData

object ImageProcessing {

  implicit class ImageDataOps(source: ImageData) {
    val width: Int = source.width.toInt
    val height: Int = source.height.toInt
    val numPix: Int = (source.data.length / 4).toInt
    assert(source.data.length % 4 == 0)

    @inline
    def r(x: Int, y: Int): Double = source.data(((y * width) + x) * 4).getOrElse(255D)
    @inline
    def g(x: Int, y: Int): Double = source.data((((y * width) + x) * 4) + 1).getOrElse(255D)
    @inline
    def b(x: Int, y: Int): Double = source.data((((y * width) + x) * 4) + 2).getOrElse(255D)

    @inline
    def putPixel(x: Int, y: Int, r: Double, g: Double, b: Double): Unit = {
      val i = ((y * width) + x) * 4
      source.data(i) = r
      source.data(i + 1) = g
      source.data(i + 2) = b
    }

    def grayscale(): Unit = {
      for (y: Int <- 0 until height) {
        for (x: Int <- 0 until width) {
          val avg = (r(x,y) + g(x,y) + b(x,y)) / 3
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
