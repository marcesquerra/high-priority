import simulacrum._
import scala.language.implicitConversions

import com.bryghts.highpriority._

@typeclass trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object Functor extends ProvidedTCF[Priority1, Functor] {
  trait FunctorImplicits extends ToFunctorOps
}

@typeclass trait Applicative[T[_]] { self: Functor[T] =>
}

object Applicative extends ProvidedTCF[Priority10, Applicative] {
  trait ApplicativeImplicits extends ToApplicativeOps {
    implicit def toFunctor[T[_]](implicit b: Applicative[T]): To[Functor[T]] =
      impl
  }
}

@typeclass trait Monad[F[_]] { self: Applicative[F] =>
  def pure[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object Monad extends ProvidedTCF[Priority20, Monad] {

  trait MonadImplicits extends ToMonadOps {
    implicit def toApplicative[T[_]](implicit c: Monad[T]): To[Applicative[T]] =
      impl
    implicit def toFunctor[T[_]](implicit b: Monad[T]): To[Functor[T]] = impl
  }
}

@typeclass trait Traverse[T[_]] { self: Functor[T] =>
  def c: String = "Traverse"
}
object Traverse extends ProvidedTCF[Priority30, Traverse] {

  trait TraverseImplicits extends ToTraverseOps {
    implicit def toFunctor[T[_]](implicit b: Traverse[T]): To[Functor[T]] = impl
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
