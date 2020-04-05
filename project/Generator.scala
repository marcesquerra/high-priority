package bryghts

import sbt._
import Keys._

object Generator {

  def generate(sources: File) = {

    val MAX_PROVIDERS = 256

    val root = sources / "com" / "bryghts" / "highpriority"

    val `Provider.scala` = root / "Provider.scala"
    IO.write(`Provider.scala`, generateProviders(MAX_PROVIDERS))

    val `FullProvider.scala` = root / "FullProvider.scala"
    IO.write(`FullProvider.scala`, generateFullProvider(MAX_PROVIDERS))

    Seq(`Provider.scala`, `FullProvider.scala`)
  }

  def generateProviders(count: Int): String = {
    def provider(n: Int): String =
      s"""case class Provider$n[T](provide: T)"""

    val providers = (1 to count).map(provider).mkString("\n")

    s"""|package com.bryghts.highpriority
        |
        |$providers""".stripMargin
  }

  def generateFullProvider(count: Int): String = {
    def geneareteImplicitBlock(n: Int): String = {
      def extension =
        if (n == count) ""
        else s" extends FullProviderImplicitsAtPriority${n + 1}"

      s"""|
          |trait FullProviderImplicitsAtPriority${n}$extension {
          |  implicit def provide${n}[T](implicit p${n}: Provider${n}[T]): FullProvider[T] =
          |    FullProvider(p${n}.provide)
          |}""".stripMargin
    }

    val implicits = (1 to count).map(geneareteImplicitBlock).mkString("\n")

    s"""|package com.bryghts.highpriority
        |
        |case class FullProvider[T](provide: T)
        |
        |object FullProvider extends FullProviderImplicitsAtPriority1
        |
        |$implicits""".stripMargin
  }

}
