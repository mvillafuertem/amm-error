import scala.sys.process.Process

name := "amm-error"

version := "0.1"

val scala3 = "3.0.2"

scalaVersion := scala3

crossScalaVersions := Seq(scala3)

libraryDependencies ++= Seq("com.lihaoyi" % "ammonite" % "2.4.1" % Test cross CrossVersion.full exclude("com.lihaoyi", "sourcecode_2.13"))

commands += Command.args("amm", "<scriptClass>") { (state, args) =>
  val cp = getClasspathAsJars(state).mkString(":")
  Process(s"java -classpath ${cp} ammonite.Main ${args.mkString(" ")}").!
  state
}


def getClasspathAsJars(state: sbt.State) = Project
  .runTask(Test / dependencyClasspathAsJars, state)
  .get
  ._2
  .toEither
  .fold(
    exception => throw exception,
    value => value.map(_.data.getPath)
  )
