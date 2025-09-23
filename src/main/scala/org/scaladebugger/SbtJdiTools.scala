package org.scaladebugger

import java.io.FileNotFoundException
import org.scaladebugger.Compat._

import sbt._
import sbt.Keys._

import scala.sys.process._

import scala.util.Try

/**
 * Represents the main plugin to add tooling settings to projects using
 * libraries from the Scala Debugger project.
 */
object SbtJdiTools extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[?]] = inConfig(Compile)(settings)

  lazy val settings = baseScalaDebuggerToolsSettings

  lazy val baseScalaDebuggerToolsSettings: Seq[Def.Setting[?]] =
    if (System.getProperty("java.specification.version").startsWith("1."))
      Seq(
        // JDK Dependency (just for sbt, must exist on classpath for execution,
        // cannot be redistributed)
        unmanagedJars += Def.uncached(Compat.toClassEntry(JavaTools))
      )
    else
      // on Java 9+, we don't need to do anything at all
      Seq()

  //
  // NOTE: Taken from Ensime Server project (when under BSD 3-clause)
  // https://github.com/ensime/ensime-server/blob/master/project/EnsimeBuild.scala
  //
  // WORKAROUND: https://github.com/typelevel/scala/issues/75
  lazy val JavaTools: File = List(
    // manual
    sys.env.get("JDK_HOME"),
    sys.env.get("JAVA_HOME"),
    // osx
    Try("/usr/libexec/java_home".!!).toOption,
    // fallback
    sys.props.get("java.home").map(new File(_).getParent),
    sys.props.get("java.home")
  ).flatten.map { n =>
    new File(n.trim + "/lib/tools.jar")
  }.find(_.exists()).getOrElse(
    throw new FileNotFoundException("""
      |Could not automatically find the JDK/lib/tools.jar.
      |You must explicitly set JDK_HOME or JAVA_HOME.
    """.stripMargin)
  )
}
