package com.github.andriimartynov.crdt.instances

import cats.Functor
import cats.kernel.{ BoundedSemilattice, Eq, Monoid }
import cats.syntax.all._
import com.github.andriimartynov.crdt.TwoPSet.TwoPSetState
import com.github.andriimartynov.crdt.TwoPSet

trait SetInstancesBinCompat0 {

  implicit def catsStdBoundedSemilatticeForTwoPSetState[T](implicit
    b: BoundedSemilattice[Set[T]]
  ): BoundedSemilattice[TwoPSetState[T]] =
    new BoundedSemilattice[TwoPSetState[T]] {
      override def combine(
        a1: TwoPSetState[T],
        a2: TwoPSetState[T]
      ): TwoPSetState[T] =
        TwoPSetState(
          a1.added |+| a2.added,
          a1.removed |+| a2.removed
        )

      override def empty: TwoPSetState[T] = TwoPSetState[T](b.empty, b.empty)

    }

  implicit def catsStdEqForTwoPSetState[T](implicit
    e: Eq[Set[T]]
  ): Eq[TwoPSetState[T]] =
    Eq.instance[TwoPSetState[T]] {
      case (a1, a2) =>
        a1.added === a2.added &&
          a1.removed === a2.removed
    }

  implicit def catsStdFunctorForTwoPSetState[T]: Functor[TwoPSetState] =
    new Functor[TwoPSetState] {
      def map[A, B](fa: TwoPSetState[A])(f: A => B): TwoPSetState[B] =
        fa.copy(added = fa.added.map(f), removed = fa.removed.map(f))

    }

  implicit def setsTwoPSetInstance[T]: TwoPSet[T] =
    new TwoPSet[T] {
      override def add(t: TwoPSetState[T], op: T)(implicit
        m: Monoid[TwoPSetState[T]]
      ): TwoPSetState[T] =
        t.copy(t.added + op)

      override def contains(t: TwoPSetState[T], elem: T): Boolean =
        t.added.contains(elem) && !t.removed.contains(elem)

      override def remove(t: TwoPSetState[T], op: T)(implicit
        m: Monoid[TwoPSetState[T]]
      ): TwoPSetState[T] =
        t.copy(removed = t.removed + op)

      override def merge(t1: TwoPSetState[T], t2: TwoPSetState[T])(implicit
        b: BoundedSemilattice[TwoPSetState[T]]
      ): TwoPSetState[T] =
        t1 |+| t2

    }

}
