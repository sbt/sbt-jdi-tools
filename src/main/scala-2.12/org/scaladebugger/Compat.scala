package org.scaladebugger

import sbt._

private[scaladebugger] object Compat {
  def toClassEntry(file: File): Attributed[File] = Attributed.blank(file)

  implicit class DefOps(private val self: sbt.Def.type) extends AnyVal {
    def uncached[A](a: A): A = a
  }
}
