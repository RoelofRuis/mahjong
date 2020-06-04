package app.modules.capture

object Model {

  final case class DetectionModel(
    horizontalWindow: Int,
    verticalWindow: Int,
    zoom: Double,
  )

}
