enablePlugins(ScalaJSPlugin)

name := "mahjong"
scalaVersion := "2.13.1"
version := "0.1"
scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "scalatags" % "0.9.1",
  "com.lihaoyi" %%% "upickle" % "1.1.0",
  "org.scala-js" %%% "scalajs-dom" % "1.0.0"
)