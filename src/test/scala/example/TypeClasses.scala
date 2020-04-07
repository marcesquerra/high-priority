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

object Applicative extends ProvidedTCF1[Priority10, Applicative, Functor] {
  trait ApplicativeImplicits extends PriorityImplicits with ToApplicativeOps
}

@typeclass trait Monad[F[_]] { self: Applicative[F] =>
  def pure[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object Monad extends ProvidedTCF2[Priority20, Monad, Applicative, Functor] {
  trait MonadImplicits extends PriorityImplicits with ToMonadOps
}

@typeclass trait Traverse[T[_]] { self: Functor[T] =>
  def c: String = "Traverse"
}
object Traverse extends ProvidedTCF1[Priority30, Traverse, Functor] {
  trait TraverseImplicits extends PriorityImplicits with ToTraverseOps
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
