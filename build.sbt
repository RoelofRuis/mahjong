import java.nio.file.{Files, StandardCopyOption}

lazy val mahjong = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalablyTypedConverterPlugin)
  .configure(browserProject)
  .settings(
    name := "mahjong",
    scalaVersion := "2.13.1",
    version := "0.1",
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
      val artifacts = (Compile / fastOptJS / webpack).value
      val artifactFolder = (Compile/ fastOptJS / crossTarget).value
      val distFolder = (ThisBuild / baseDirectory).value / "dist"

      val bundleFile = artifacts
        .find(_.metadata.get(BundlerFileTypeAttr).exists(_ == BundlerFileType.ApplicationBundle))
        .get
        .data

      val target = bundleFile.relativeTo(artifactFolder) match {
        case None          => distFolder / bundleFile.name
        case Some(relFile) => distFolder / relFile.toString
      }

      Files.copy(bundleFile.toPath, target.toPath, StandardCopyOption.REPLACE_EXISTING)
    }
  )