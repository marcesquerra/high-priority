import simulacrum._
import scala.language.implicitConversions

import com.bryghts.highpriority._

@typeclass trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object Functor extends ProvidedTCF[Functor] {
  trait FunctorImplicits extends ToFunctorOps
}

@typeclass trait Applicative[T[_]] { self: Functor[T] =>
}

object Applicative extends ProvidedTCF[Applicative] {
  trait ApplicativeImplicits extends ToApplicativeOps {
    implicit def functorProvider1[T[_]](
        implicit b: Applicative[T]
    ): Provider1[Functor[T]] = Provider1(b.asInstanceOf[Functor[T]])
  }
}

@typeclass trait Monad[F[_]] { self: Applicative[F] =>
  def pure[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object Monad extends ProvidedTCF[Monad] {

  trait MonadImplicits extends ToMonadOps {
    implicit def provider1[T[_]](
        implicit c: Monad[T]
    ): Provider1[Applicative[T]] = Provider1(c.asInstanceOf[Applicative[T]])
    implicit def functorProvider2[T[_]](
        implicit b: Monad[T]
    ): Provider2[Functor[T]] = Provider2(b.asInstanceOf[Functor[T]])
  }
}

@typeclass trait Traverse[T[_]] { self: Functor[T] =>
  def c: String = "Traverse"
}
object Traverse extends ProvidedTCF[Traverse] {

  trait TraverseImplicits extends ToTraverseOps {
    implicit def functorProvider3[T[_]](
        implicit b: Traverse[T]
    ): Provider3[Functor[T]] = Provider3(b.asInstanceOf[Functor[T]])
  }
}

package object typeclasses
    extends Monad.MonadImplicits
    with Traverse.TraverseImplicits
    with Applicative.ApplicativeImplicits
    with Functor.FunctorImplicits

//////////////////////////////////////////////////////////////////////

object Foo {

  import typeclasses._

  def foo1[F[_]: Monad]: F[Int] =
    for {
      a <- Monad[F].pure(10)
      b <- Monad[F].pure(20)
    } yield a + b

  def foo2[F[_]: Monad: Traverse]: F[Int] =
    for {
      a <- Monad[F].pure(10)
      b <- Monad[F].pure(20)
    } yield a + b

  def foo3[F[_]: Monad: Traverse: Functor]: F[Int] =
    for {
      a <- Monad[F].pure(10)
      b <- Monad[F].pure(20)
    } yield a + b

  def foo4[F[_]: Monad: Functor]: F[Int] =
    for {
      a <- Monad[F].pure(10)
      b <- Monad[F].pure(20)
    } yield a + b

}
