/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under Apache License 2.0
         /___/                           Please report bugs and contribute back your improvements

##################################*/
import sbt.url

name := "scala-commons"
ThisBuild / version := "0.3.0-SNAPSHOT"
ThisBuild / organization := "com.tegonal"
ThisBuild / description := "A library containing utility and helper functions for scala"

ThisBuild / scalaVersion := "3.4.1"

lazy val root = (project in file("."))
  .settings(
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M12" % Test
  )

ThisBuild / organizationName := "Tegonal Genossenschaft"
ThisBuild / organizationHomepage := Some(url("http://tegonal.com"))
ThisBuild / homepage := Some(url("https://github.com/tegonal/scala-commons"))

ThisBuild / licenses := List(
  "Apache License, Version 2.0" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
Compile / unmanagedResourceDirectories += baseDirectory.value
Compile / unmanagedResources / includeFilter := "LICENSE.txt"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/tegonal/scala-commons"),
    connection = "scm:git:git://github.com/tegonal/scala-commons.git",
    devConnection = "scm:git:git://github.com/tegonal/scala-commons.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "robstoll",
    name = "Robert Stoll",
    email = "rstoll@tutteli.ch",
    url = url("http://tutteli.ch")
  )
)

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true
ThisBuild / versionScheme := Some("early-semver")

credentials += Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env.getOrElse("SONATYPE_USER", ""),
  sys.env.getOrElse("SONATYPE_PW", "")
)
Global / pgpSigningKey := Some("2D7B7171CE32D107")

// The next release must be binary and source compatible with the previous release (it will be a patch release)
ThisBuild / versionPolicyIntention := Compatibility.BinaryAndSourceCompatible
