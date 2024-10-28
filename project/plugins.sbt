logLevel := Level.Warn

// Used for signing in order to publish jars
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")

// Used to ensure proper publish process is followed
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")

addSbtPlugin("com.github.sbt" % "sbt-github-actions" % "0.24.0")
