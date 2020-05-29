import java.nio.file.{Files, StandardCopyOption}

lazy val mahjong = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalablyTypedConverterPlugin)
  .configure(browserProject)
  .settings(
    name := "mahjong",
    scalaVersion := "2.13.1",
    version := "0.1",
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    scalaJSUseMainModuleInitializer := true,

    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalatags" % "0.9.1",
      "com.lihaoyi" %%% "upickle" % "1.1.0",
    ),

    Compile / npmDependencies ++= Seq(
      "@types/w3c-image-capture" -> "1.0.2"
    ),

    useYarn := true,

    stStdlib := List("es6"),
    stUseScalaJsDom := false,
  )

lazy val start = TaskKey[Unit]("start")
lazy val dist = TaskKey[Unit]("dist")

lazy val browserProject: Project => Project =
  _.settings(
    start := {
      (Compile / fastOptJS / startWebpackDevServer).value
    },
    dist := {
      val artifacts = (Compile / fullOptJS / webpack).value
      val artifactFolder = (Compile/ fullOptJS / crossTarget).value
      val distFolder = (ThisBuild / baseDirectory).value / "dist"

      artifacts.foreach { artifact =>
        val target = artifact.data.relativeTo(artifactFolder) match {
          case None          => distFolder / artifact.data.name
          case Some(relFile) => distFolder / relFile.toString
        }

        Files.copy(artifact.data.toPath, target.toPath, StandardCopyOption.REPLACE_EXISTING)
      }
    }
  )