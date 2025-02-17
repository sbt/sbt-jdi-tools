# Sbt Tools Jar Plugin

Looks up and adds the JDK `tools.jar` to your sbt's unmanaged jars, enabling
access to the Java Debugger Interface (JDI).

## Installation

### sbt 1.0 and 2.x

Add the following to `project/plugins.sbt`:

```scala
addSbtPlugin("com.github.sbt" % "sbt-jdi-tools" % "1.2.0")
```

### sbt 0.13.x

Add the following to `project/plugins.sbt`:

```scala
addSbtPlugin("org.scala-debugger" % "sbt-jdi-tools" % "1.0.1")
```
