package bryghts

import sbt._
import Keys._

object Generator {

  def generate(sources: File) = {

    val MAX_PROVIDERS = 256

    val root = sources / "com" / "bryghts" / "highpriority"

    // val `Provider.scala` = root / "Provider.scala"
    // IO.write(`Provider.scala`, generateProviders(MAX_PROVIDERS))

    val `Priority.scala` = root / "Priority.scala"
    IO.write(`Priority.scala`, generatePriorities(MAX_PROVIDERS))

    val `FullProvider.scala` = root / "FullProvider.scala"
    IO.write(`FullProvider.scala`, generateFullProvider(MAX_PROVIDERS))

    Seq(`Priority.scala`, `FullProvider.scala`)
  }

  def generatePriorities(count: Int): String = {
    def priority(n: Int): String =
      s"""sealed trait Priority$n extends Priority"""

    val priorities = (1 to count).map(priority).mkString("\n")

    s"""|package com.bryghts.highpriority
        |
        |sealed trait Priority
        |
        |$priorities""".stripMargin
  }

  // def generateProviders(count: Int): String = {
  //   def provider(n: Int): String =
  //     s"""case class Provider$n[T](provide: T)"""

  //   val providers = (1 to count).map(provider).mkString("\n")

  //   s"""|package com.bryghts.highpriority
  //       |
  //       |$providers""".stripMargin
  // }

  def generateFullProvider(count: Int): String = {
    def geneareteImplicitBlock(n: Int): String = {
      def extension =
        if (n == count) ""
        else s" extends FullProviderImplicitsAtPriority${n + 1}"

      s"""|
          |trait FullProviderImplicitsAtPriority${n}$extension {
          |  implicit def provide${n}[T](implicit p${n}: Provider[Priority${n}, T]): FullProvider[T] =
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
