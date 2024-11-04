package org.scaladebugger

import sbt._

private[scaladebugger] object Compat {
  def toClassEntry(file: File): Attributed[File] = Attributed.blank(file)
}
