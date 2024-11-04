
organization := "com.github.sbt"

name := "sbt-jdi-tools"

enablePlugins(SbtPlugin)

version := "1.1.1"

licenses += (
  "Apache-2.0",
  url("https://www.apache.org/licenses/LICENSE-2.0.html")
)

homepage := Some(url("https://github.com/sbt/sbt-jdi-tools"))

scalaVersion := "2.12.20"
pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" =>
      "1.5.8"
    case _ =>
      "2.0.0-M2"
  }
}

scalacOptions ++= Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8",
  "-deprecation", "-unchecked", "-feature",
  "-Xfatal-warnings"
) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, scalaMajor)) if scalaMajor == 10 => Seq("-Ywarn-all")
  case _ => Nil
})

javacOptions ++= Seq(
  "-source", "1.8", "-target", "1.8", "-Xlint:all", "-Werror",
  "-Xlint:-options", "-Xlint:-path", "-Xlint:-processing"
)

scalacOptions in (Compile, doc) ++= Seq(
  "-no-link-warnings" // Suppress problems with Scaladoc @throws links
)

// Properly handle Scaladoc mappings
autoAPIMappings := true

// Prevent publishing test artifacts
publishArtifact in Test := false

publishMavenStyle := true

pomIncludeRepository := { _ => false }

pomExtra :=
  <scm>
    <url>git@github.com:ensime/sbt-jdi-tools.git</url>
    <connection>scm:git:git@github.com:ensime/sbt-jdi-tools.git</connection>
  </scm>
    <developers>
      <developer>
        <id>senkwich</id>
        <name>Chip Senkbeil</name>
        <url>http://www.chipsenkbeil.org</url>
      </developer>
    </developers>

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

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
