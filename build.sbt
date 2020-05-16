enablePlugins(ScalaJSPlugin)

name := "mahjong"
version := "0.1"
scalaVersion := "2.13.1"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "1.0.0",
  "com.lihaoyi" %%% "scalatags" % "0.9.1",
  "com.lihaoyi" %%% "upickle" % "1.1.0",
)