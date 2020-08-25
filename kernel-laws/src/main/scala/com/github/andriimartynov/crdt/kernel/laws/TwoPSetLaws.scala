package com.github.andriimartynov.crdt.kernel.laws

import cats.Monoid
import cats.kernel.BoundedSemilattice
import cats.kernel.laws.{ IsEq, IsEqArrow }
import com.github.andriimartynov.crdt.kernel.sets.TwoPSet
import com.github.andriimartynov.crdt.kernel.sets.TwoPSet.TwoPSetState

trait TwoPSetLaws[T] extends CRDTLaws[TwoPSetState[T], T] {
  implicit def S: TwoPSet[T]

  implicit def B: BoundedSemilattice[TwoPSetState[T]]

  implicit def M: Monoid[T]

  def add0(x: TwoPSetState[T]): IsEq[TwoPSetState[T]] =
    S.add(x, M.empty) <-> x.copy(added = x.added + M.empty)

  def contains0(x: TwoPSetState[T]): IsEq[Boolean] =
    if (x.added.isEmpty && x.removed.isEmpty) S.contains(x, M.empty) <-> false
    else true <-> true

  def contains1(x: TwoPSetState[T]): IsEq[Boolean] =
    if (x.added.nonEmpty && x.removed.isEmpty) S.contains(x, x.added.head) <-> true
    else true <-> true

  def contains2(x: TwoPSetState[T]): IsEq[Boolean] =
    if (x.removed.nonEmpty) S.contains(x, x.removed.head) <-> false
    else true <-> true

  def remove(x: TwoPSetState[T]): IsEq[TwoPSetState[T]] =
    S.remove(x, M.empty) <-> x.copy(removed = x.removed + M.empty)

  def merge(x1: TwoPSetState[T], x2: TwoPSetState[T]): IsEq[TwoPSetState[T]] =
    S.merge(x1, x2) <-> x1.copy(added = x1.added ++ x2.added, removed = x1.removed ++ x2.removed)

}

object TwoPSetLaws {
  def apply[T](implicit
    s: TwoPSet[T],
    b: BoundedSemilattice[TwoPSetState[T]],
    m: Monoid[T]
  ): TwoPSetLaws[T] =
    new TwoPSetLaws[T] {
      def S: TwoPSet[T]                          = s
      def B: BoundedSemilattice[TwoPSetState[T]] = b
      def M: Monoid[T]                           = m
    }

}
