name := "play-mongodb-gridfs"
version := "1.0.0"

scalaVersion := "2.11.7"
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

javaOptions in Test += "-Dconfig.file=conf/test.conf"

libraryDependencies += specs2
libraryDependencies += ws

// Mongodb scala driver
libraryDependencies += "org.mongodb" % "casbah-core_2.11" % "3.1.1"
libraryDependencies += "org.mongodb" % "casbah-query_2.11" % "3.1.1"
libraryDependencies += "org.mongodb" % "casbah-commons_2.11" % "3.1.1"
libraryDependencies += "org.mongodb" % "casbah-gridfs_2.11" % "3.1.1"