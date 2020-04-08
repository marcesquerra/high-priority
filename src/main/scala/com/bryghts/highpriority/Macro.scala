// package com.bryghts.highpriority

import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation
import scala.annotation.compileTimeOnly
import com.bryghts.highpriority._

@compileTimeOnly("enable macro paradise to expand macro annotations")
class provided[P <: Priority] extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro providedMacro.impl[P]
}

object providedMacro {
  def impl[P <: Priority](c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {

    import c.universe._

    val inputs = annottees.map(_.tree).toList
    def error = c.abort(
      c.enclosingPosition,
      s"The 'provided' annotation can only be used on traits"
    )

    inputs match {
      case (param: ClassDef) :: rest if rest.length < 2 =>
        param match {
          case q"$_ trait $tpname[..$tparams] extends { ..$_ } with ..$_ { $_: $selft => ..$_ }" =>
            val priority = c.prefix.tree match {
              case q"new ${_}[$t]" =>
                val t2 = t.duplicate
                val t3 = c.typeCheck(q"(??? : $t2)").tpe
                if (t3 <:< weakTypeOf[Priority]) t3
                else
                  c.abort(
                    c.enclosingPosition,
                    s"'provided' requires a valid priority as a type parameter. Got '${t}' instead"
                  )
              case _ =>
                c.abort(
                  c.enclosingPosition,
                  s"Couldn't get priority from '${showRaw(c.prefix.tree)}'"
                )
            }

            val selfTypes: List[TypeName] = (selft match {
              case tq"" => Nil
              case tq"..$tps { ..$_ }" =>
                tps
              case _ =>
                List(selft)
            }).map {
              case TypeDef(_, n, _, _)          => n
              case AppliedTypeTree(Ident(n), _) => TypeName(n.toString)
              case other =>
                c.abort(
                  c.enclosingPosition,
                  s"Couldn't extract the self types from '${showRaw(other)}'"
                )
            }
            val name = tpname.toString
            val traitName = "From" + name + "Providers"
            val (mods, earlydefns, parents, self, body) = rest match {
              case q"$mods object $_ extends { ..$earlydefns } with ..$parents { $self => ..$body }" :: Nil =>
                (mods, earlydefns, parents, self, body)
              case _ =>
                (Modifiers(), Nil, Nil, q"", Nil)
            }
            val tnames =
              tparams.map { case TypeDef(_, n, _, _) => n }
            val appliedType =
              tq"$tpname[..$tnames]"
            val providers =
              selfTypes.map { t =>
                val funcName = TermName(s"from${name}To$t")
                val appliedResponseType =
                  tq"$t[..$tnames]"
                q"""
                  implicit def $funcName[..$tparams](implicit p: $appliedType): Provider[$priority, $appliedResponseType] =
                    Provider(p.asInstanceOf[$appliedResponseType])
                """
              }

            val newObject =
              q"""
                $mods object ${TermName(name)} extends { ..$earlydefns } with ..$parents { $self =>

                  implicit def fromProvider[..$tparams](implicit p: FullProvider[$appliedType]): $appliedType =
                    p.provide

                  trait ${TypeName(traitName)} {
                    ..$providers
                  }

                  ..$body
                }
              """

            c.Expr(q"""
              $param
              $newObject
            """)

          case _ =>
            error
        }
      case _ =>
        error
    }

  }
}
