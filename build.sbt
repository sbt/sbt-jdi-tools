
organization := "com.github.sbt"
name := "sbt-jdi-tools"

enablePlugins(SbtPlugin)

licenses += (
  "Apache-2.0",
  url("https://www.apache.org/licenses/LICENSE-2.0.html")
)
homepage := Some(url("https://github.com/sbt/sbt-jdi-tools"))
developers := List(
  Developer(
    "senkwich",
    "Chip Senkbeil",
    "chip.senkbeil@gmail.com",
    url("http://www.chipsenkbeil.org")
  ),
  Developer(
    "adpi2",
    "Adrien Piquerez",
    "adrien.piquerez@gmail.com",
    url("https://github.com/adpi2/")
  )
)

val scala212 = "2.12.20"
val scala3 = "3.7.3"

ThisBuild / scalaVersion := scala212
ThisBuild / crossScalaVersions := Seq(scala212, scala3)
pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" =>
      "1.5.8"
    case _ =>
      "2.0.0-RC6"
  }
}

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation", "-unchecked", "-feature",
  "-Xfatal-warnings"
) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, _)) => Seq("-target:jvm-1.8")
  case _ => Nil
})

javacOptions ++= Seq(
  "-source", "1.8", "-target", "1.8", "-Xlint:all", "-Werror",
  "-Xlint:-options", "-Xlint:-path", "-Xlint:-processing"
)

Compile / doc / scalacOptions ++= Seq(
  "-no-link-warnings" // Suppress problems with Scaladoc @throws links
)

// Properly handle Scaladoc mappings
autoAPIMappings := true

// Prevent publishing test artifacts
Test / publishArtifact := false

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(
    RefPredicate.StartsWith(Ref.Tag("v"))
  )
ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish project"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
    )
  )
)
