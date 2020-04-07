package com.bryghts.highpriority

trait Provided[P, T] {
  implicit def fromProvider(implicit p: FullProvider[T]): T =
    p.provide

  type To[A] = Provider[P, A]
  def impl[B](implicit t: T): To[B] =
    Provider(t.asInstanceOf[B])
}

trait Provided1[P, T, A] extends Provided[P, T] {
  trait PriorityImplicits {
    def toA(implicit t: T): To[A] = impl
  }
}

trait Provided2[P, T, A, B] extends Provided[P, T] {
  trait PriorityImplicits {
    def toA(implicit t: T): To[A] = impl
    def toB(implicit t: T): To[B] = impl
  }
}

trait Provided3[P, T, A, B, C] extends Provided[P, T] {
  trait PriorityImplicits {
    def toA(implicit t: T): To[A] = impl
    def toB(implicit t: T): To[B] = impl
    def toC(implicit t: T): To[C] = impl
  }
}

trait Provided4[P, T, A, B, C, D] extends Provided[P, T] {
  trait PriorityImplicits {
    def toA(implicit t: T): To[A] = impl
    def toB(implicit t: T): To[B] = impl
    def toC(implicit t: T): To[C] = impl
    def toD(implicit t: T): To[D] = impl
  }
}

trait Provided5[P, T, A, B, C, D, E] extends Provided[P, T] {
  trait PriorityImplicits {
    def toA(implicit t: T): To[A] = impl
    def toB(implicit t: T): To[B] = impl
    def toC(implicit t: T): To[C] = impl
    def toD(implicit t: T): To[D] = impl
    def toE(implicit t: T): To[E] = impl
  }
}

trait Provided6[P, T, A, B, C, D, E, F] extends Provided[P, T] {
  trait PriorityImplicits {
    def toA(implicit t: T): To[A] = impl
    def toB(implicit t: T): To[B] = impl
    def toC(implicit t: T): To[C] = impl
    def toD(implicit t: T): To[D] = impl
    def toE(implicit t: T): To[E] = impl
    def toF(implicit t: T): To[F] = impl
  }
}

trait ProvidedTC[P, TC[_]] {
  implicit def fromProvider[A](implicit p: FullProvider[TC[A]]): TC[A] =
    p.provide

  type To[A] = Provider[P, A]
  def impl[A, B](implicit a: TC[A]): To[B] =
    Provider(a.asInstanceOf[B])
}

trait ProvidedTC1[P, T[_], A[_]] extends ProvidedTC[P, T] {
  trait PriorityImplicits {
    def toA[U](implicit t: T[U]): To[A[U]] = impl
  }
}

trait ProvidedTC2[P, T[_], A[_], B[_]] extends ProvidedTC[P, T] {
  trait PriorityImplicits {
    def toA[U](implicit t: T[U]): To[A[U]] = impl
    def toB[U](implicit t: T[U]): To[B[U]] = impl
  }
}

trait ProvidedTC3[P, T[_], A[_], B[_], C[_]] extends ProvidedTC[P, T] {
  trait PriorityImplicits {
    def toA[U](implicit t: T[U]): To[A[U]] = impl
    def toB[U](implicit t: T[U]): To[B[U]] = impl
    def toC[U](implicit t: T[U]): To[C[U]] = impl
  }
}

trait ProvidedTC4[P, T[_], A[_], B[_], C[_], D[_]] extends ProvidedTC[P, T] {
  trait PriorityImplicits {
    def toA[U](implicit t: T[U]): To[A[U]] = impl
    def toB[U](implicit t: T[U]): To[B[U]] = impl
    def toC[U](implicit t: T[U]): To[C[U]] = impl
    def toD[U](implicit t: T[U]): To[D[U]] = impl
  }
}

trait ProvidedTC5[P, T[_], A[_], B[_], C[_], D[_], E[_]]
    extends ProvidedTC[P, T] {
  trait PriorityImplicits {
    def toA[U](implicit t: T[U]): To[A[U]] = impl
    def toB[U](implicit t: T[U]): To[B[U]] = impl
    def toC[U](implicit t: T[U]): To[C[U]] = impl
    def toD[U](implicit t: T[U]): To[D[U]] = impl
    def toE[U](implicit t: T[U]): To[E[U]] = impl
  }
}

trait ProvidedTC6[P, T[_], A[_], B[_], C[_], D[_], E[_], F[_]]
    extends ProvidedTC[P, T] {
  trait PriorityImplicits {
    def toA[U](implicit t: T[U]): To[A[U]] = impl
    def toB[U](implicit t: T[U]): To[B[U]] = impl
    def toC[U](implicit t: T[U]): To[C[U]] = impl
    def toD[U](implicit t: T[U]): To[D[U]] = impl
    def toE[U](implicit t: T[U]): To[E[U]] = impl
    def toF[U](implicit t: T[U]): To[F[U]] = impl
  }
}

trait ProvidedTCF[P, TC[F[_]]] {
  implicit def fromProvider[F[_], A](implicit p: FullProvider[TC[F]]): TC[F] =
    p.provide

  type To[A] = Provider[P, A]
  def impl[F[_], B](implicit a: TC[F]): To[B] =
    Provider(a.asInstanceOf[B])
}

trait ProvidedTCF1[P, TC[F[_]], A[F[_]]] extends ProvidedTCF[P, TC] {
  trait PriorityImplicits {
    def toA[F[_]](implicit t: TC[F]): To[A[F]] = impl
  }
}

trait ProvidedTCF2[P, TC[F[_]], A[F[_]], B[F[_]]] extends ProvidedTCF[P, TC] {
  trait PriorityImplicits {
    def toA[F[_]](implicit t: TC[F]): To[A[F]] = impl
  }
}

trait ProvidedTCF3[P, TC[F[_]], A[F[_]], B[F[_]], C[F[_]]]
    extends ProvidedTCF[P, TC] {
  trait PriorityImplicits {
    def toA[F[_]](implicit t: TC[F]): To[A[F]] = impl
  }
}

trait ProvidedTCF4[P, TC[F[_]], A[F[_]], B[F[_]], C[F[_]], D[F[_]]]
    extends ProvidedTCF[P, TC] {
  trait PriorityImplicits {
    def toA[F[_]](implicit t: TC[F]): To[A[F]] = impl
  }
}

trait ProvidedTCF5[P, TC[F[_]], A[F[_]], B[F[_]], C[F[_]], D[F[_]], E[F[_]]]
    extends ProvidedTCF[P, TC] {
  trait PriorityImplicits {
    def toA[F[_]](implicit t: TC[F]): To[A[F]] = impl
  }
}

trait ProvidedTCF6[P, TC[F[_]], A[F[_]], B[F[_]], C[F[_]], D[F[_]], E[F[_]], F[
    F[_]
]] extends ProvidedTCF[P, TC] {
  trait PriorityImplicits {
    def toA[F[_]](implicit t: TC[F]): To[A[F]] = impl
  }
}
