package bryghts

import sbt._
import Keys._

object Generator {

  def generate(sources: File) = {

    val root = sources / "com" / "bryghts" / "highpriority"

    val `Provider.scala` = root / "Provider.scala"
    IO.write(`Provider.scala`, generateProviders)
    Seq(`Provider.scala`)
  }

  def generateProviders: String = {
    def provider(n: Int): String =
      s"""case class Provider$n[T](provide: T)"""

    val providers = (1 to 256).map(provider).mkString("\n")

    s"""|package com.bryghts.highpriority
        |
        |$providers""".stripMargin
  }
}
