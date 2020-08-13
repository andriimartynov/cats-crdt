package com.github.andriimartynov.crdt.instances

import cats.kernel.{ BoundedSemilattice, Monoid }
import cats.syntax.semigroup._
import com.github.andriimartynov.crdt.GSet

trait SetInstances {
  implicit def catsStdBoundedSemilatticeForSet[T](implicit
    b: BoundedSemilattice[T]
  ): BoundedSemilattice[Set[T]] =
    new BoundedSemilattice[Set[T]] {
      override def combine(
        a1: Set[T],
        a2: Set[T]
      ): Set[T] = a1 union a2

      override def empty: Set[T] = Set.empty[T]

    }

  implicit def setsGSetInstance[T]: GSet[T] =
    new GSet[T] {
      override def add(t: Set[T], op: T)(implicit
        m: Monoid[Set[T]]
      ): Set[T] = t + op

      override def merge(t1: Set[T], t2: Set[T])(implicit
        b: BoundedSemilattice[Set[T]]
      ): Set[T] = t1 |+| t2

    }

}
