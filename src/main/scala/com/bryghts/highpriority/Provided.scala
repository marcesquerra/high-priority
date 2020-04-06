package com.bryghts.highpriority

trait Provided[P, T] {
  implicit def fromProvider(implicit p: FullProvider[T]): T =
    p.provide

  type To[A] = Provider[P, A]
  def impl[B](implicit t: T): To[B] =
    Provider(t.asInstanceOf[B])
}

trait ProvidedTC[P, TC[_]] {
  implicit def fromProvider[A](implicit p: FullProvider[TC[A]]): TC[A] =
    p.provide

  type To[A] = Provider[P, A]
  def impl[A, B](implicit a: TC[A]): To[B] =
    Provider(a.asInstanceOf[B])
}

trait ProvidedTCF[P, TC[F[_]]] {
  implicit def fromProvider[F[_], A](implicit p: FullProvider[TC[F]]): TC[F] =
    p.provide

  type To[A] = Provider[P, A]
  def impl[F[_], B](implicit a: TC[F]): To[B] =
    Provider(a.asInstanceOf[B])
}
