enablePlugins(ScalaJSPlugin)

scalaVersion := "2.13.1"

lazy val mahjong = (project in file("."))
  .enablePlugins(ScalablyTypedConverterPlugin)
  .settings(
    name := "mahjong",
    version := "0.1",
    scalaJSUseMainModuleInitializer := true,

    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalatags" % "0.9.1",
      "com.lihaoyi" %%% "upickle" % "1.1.0",
    ),

    Compile / npmDependencies ++= Seq(),

    stStdlib := List("es6"),
    stUseScalaJsDom := false,
  )