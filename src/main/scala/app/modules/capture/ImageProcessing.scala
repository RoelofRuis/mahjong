package app.modules.capture

import org.scalajs.dom.ImageData

import scala.annotation.tailrec

object ImageProcessing {

  implicit class ImageDataOps(source: ImageData) {
    val width: Int = source.width
    val height: Int = source.height
    val numPix: Int = source.data.length / 4
    assert(source.data.length % 4 == 0)

    @inline
    def r(x: Int, y: Int): Double = {
      if (x < 0 || x >= width || y < 0 || y >= height) 0.0
      else source.data(((y * width) + x) * 4)
    }

    @inline
    def g(x: Int, y: Int): Double = {
      if (x < 0 || x >= width || y < 0 || y >= height) 0.0
      else source.data((((y * width) + x) * 4) + 1)
    }

    @inline
    def b(x: Int, y: Int): Double = {
      if (x < 0 || x >= width || y < 0 || y >= height) 0.0
      else source.data((((y * width) + x) * 4) + 2)
    }

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
          val avg = (r(x, y) * 0.3 + g(x, y) * 0.59 + b(x, y) * 0.11)
          putPixel(x, y, avg, avg, avg)
        }
      }
    }

    def blur(): Unit = {
      for (y: Int <- 0 until height) {
        for (x: Int <- 0 until width) {
          putPixel(x, y,
            (r(x - 1, y) + r(x, y) * 2 + r(x + 1, y)) / 4,
            (g(x - 1, y) + g(x, y) * 2 + g(x + 1, y)) / 4,
            (b(x - 1, y) + b(x, y) * 2 + b(x + 1, y)) / 4,
          )
        }
      }
      for (y: Int <- 0 until height) {
        for (x: Int <- 0 until width) {
          putPixel(x, y,
            (r(x, y - 1) + r(x, y) * 2 + r(x, y + 1)) / 4,
            (g(x, y - 1) + g(x, y) * 2 + g(x, y + 1)) / 4,
            (b(x, y - 1) + b(x, y) * 2 + b(x, y + 1)) / 4,
          )
        }
      }
    }

    @inline
    def diff(a: Double, b: Double, t: Int): Int = {
      if (a > (b + t)) 1
      else if (a < (b - t)) - 1
      else 0
    }

    def FAST(t: Int, n: Int): Unit = {
      @tailrec
      def isKeypoint(a: Array[Int], streak: Int, startStreak: Int, atStart: Boolean): Boolean = {
        if (a.isEmpty) {
          if (streak > 0 && streak >= n) true
          else if (streak < 0 && streak <= -n) true
          else if (streak > 0 && startStreak >= 0 && streak + startStreak >= n) true
          else if (streak < 0 && startStreak <= 0 && streak + startStreak <= -n) true
          else false
        }
        else {
          val elem = a.head
          if (elem == 0) {
            if (atStart) isKeypoint(a.tail, 0, streak, false)
            else isKeypoint(a.tail, 0, startStreak, false)
          }
          else if (elem == 1) {
            if (streak >= 0) {
              if (streak + 1 - n == 0) true
              else isKeypoint(a.tail, streak + 1, startStreak, atStart)
            } else {
              if (atStart) isKeypoint(a.tail, 0, streak, false)
              else isKeypoint(a.tail, 0, startStreak, false)
            }
          }
          else { // elem == -1
            if (streak <= 0) {
              if (streak - 1 + n == 0) true
              else isKeypoint(a.tail, streak - 1, startStreak, atStart)
            } else {
              if (atStart) isKeypoint(a.tail, 0, streak, false)
              else isKeypoint(a.tail, 0, startStreak, false)
            }
          }
        }
      }


      for (y: Int <- 4 until height - 4) {
        for (x: Int <- 4 until width - 4) {
          val pI = r(x, y)
          val p1 = diff(pI, r(x, y - 3), t)
          val p5 = diff(pI, r(x + 3, y), t)
          val p9 = diff(pI, r(x, y + 3), t)
          val p13 = diff(pI, r(x - 3, y), t)

          val quicksum = p1 + p5 + p9 + p13
          if (quicksum >= 3 || quicksum <= - 3) {
            val p2 = diff(pI, r(x + 1, y + 3), t)
            val p3 = diff(pI, r(x + 2, y + 2), t)
            val p4 = diff(pI, r(x + 3, y + 1), t)
            val p6 = diff(pI, r(x + 3, y - 2), t)
            val p7 = diff(pI, r(x + 2, y - 2), t)
            val p8 = diff(pI, r(x + 1, y - 3), t)
            val p10 = diff(pI, r(x - 1, y - 3), t)
            val p11 = diff(pI, r(x - 2, y - 2), t)
            val p12 = diff(pI, r(x - 3, y - 1), t)
            val p14 = diff(pI, r(x - 3, y + 1), t)
            val p15 = diff(pI, r(x - 2, y + 2), t)
            val p16 = diff(pI, r(x - 1, y + 3), t)

            val selected = isKeypoint(
              Array(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16),
              streak=0,
              startStreak=0,
              atStart=true
            )
            if (selected) {
              putPixel(x, y, 0, 255, 0)
            }
          }
        }
      }
    }
  }

}
