package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.laws.{ IsEq, IsEqArrow }
import cats.kernel.{ BoundedSemilattice, Monoid }
import com.github.andriimartynov.crdt.kernel.sets.GSet

trait GSetLaws[T] extends CRDTLaws[Set[T], T] {
  implicit def S: GSet[T]

  implicit def B: BoundedSemilattice[Set[T]]

  implicit def M: Monoid[T]

  override def add(x: Set[T]): IsEq[Set[T]] =
    S.add(x, M.empty) <-> x + M.empty

  override def merge(x1: Set[T], x2: Set[T]): IsEq[Set[T]] =
    S.merge(x1, x2) <-> x1 ++ x2

}

object GSetLaws {
  def apply[T](implicit
    s: GSet[T],
    b: BoundedSemilattice[Set[T]],
    m: Monoid[T]
  ): GSetLaws[T] =
    new GSetLaws[T] {
      def S: GSet[T]                    = s
      def B: BoundedSemilattice[Set[T]] = b
      def M: Monoid[T]                  = m
    }

}
