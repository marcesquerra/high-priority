package com.bryghts.highpriority

trait Provided[T] {
  implicit def fromProvider(implicit p: FullProvider[T]): T =
    p.provide
}

trait ProvidedTC[TC[_]] {
  implicit def fromProvider[A](implicit p: FullProvider[TC[A]]): TC[A] =
    p.provide
}

trait ProvidedTCF[TC[F[_]]] {
  implicit def fromProvider[F[_], A](implicit p: FullProvider[TC[F]]): TC[F] =
    p.provide
}
