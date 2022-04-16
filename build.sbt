ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "hazelcast-playground",
    resolvers += Resolver.jcenterRepo,
    libraryDependencies += "com.hazelcast" % "hazelcast" % "5.1.1"
  )
