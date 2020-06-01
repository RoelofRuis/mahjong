package app.modules.capture

import typings.std.ImageData
import typings.std.global.Uint8ClampedArray

object ImageProcessing {

  class ProcessableImage(source: ImageData) {
    val width: Int = source.width.toInt
    val height: Int = source.height.toInt
    assert(source.data.length % 4 == 0)

    var data: Array[(Int, Int, Int, Int)] = {
      val rawData = source.data
      Range(0, rawData.length.toInt / 4).foldRight(Array[(Int, Int, Int, Int)]()) { case (i, acc) =>
        acc :+ (
          rawData(i).get.toInt,
          rawData(i + 1).get.toInt,
          rawData(i + 2).get.toInt,
          rawData(i + 3).get.toInt
        )
      }
    }

    def updatePixels(f: ((Int, Int, Int, Int)) => (Int, Int, Int, Int)): Unit = data = data.map(f)

    def asImageData: ImageData = {
      val uintData = new Uint8ClampedArray(data.length * 4)
      data.zipWithIndex.foreach { case (i, index) =>
        val j = index * 4
        uintData(j) = i._1
        uintData(j + 1) = i._2
        uintData(j + 2) = i._3
        uintData(j + 3) = i._4
      }
      ImageData.apply(uintData, height.toDouble, width.toDouble)
    }
  }

  implicit class ProcessingOps(image: ProcessableImage) {

    def grayscale: ProcessableImage = {
      image.updatePixels { case (r, g, b, a) =>
        val avg = (r + g + b) / 3
        (avg, avg, avg, a)
      }
      image
    }

  }

}
