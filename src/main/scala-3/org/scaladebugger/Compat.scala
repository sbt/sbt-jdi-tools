package org.scaladebugger

import sbt.*

private[scaladebugger] object Compat:
  inline def toClassEntry(file: File): Attributed[HashedVirtualFileRef] =
    val fc = Keys.fileConverter.value
    Attributed.blank(fc.toVirtualFile(file.toPath))
