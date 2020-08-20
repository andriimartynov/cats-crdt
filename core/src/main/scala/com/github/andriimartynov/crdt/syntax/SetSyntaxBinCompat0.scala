package com.github.andriimartynov.crdt.syntax

import cats.kernel.{ BoundedSemilattice, Monoid }
import com.github.andriimartynov.crdt.TwoPSet
import com.github.andriimartynov.crdt.TwoPSet.TwoPSetState
import com.github.andriimartynov.crdt.instances.set._

trait SetSyntaxBinCompat0 {
  implicit final def catsSyntaxTwoPSet[T](
    state: TwoPSetState[T]
  ): TwoPSetOps[T] = new TwoPSetOps(state)
}

final class TwoPSetOps[T](private val state: TwoPSetState[T]) extends AnyVal {
  def +(
    op: T
  )(implicit
    m: Monoid[TwoPSetState[T]]
  ): TwoPSetState[T] = add(op)

  def -(
    op: T
  )(implicit
    m: Monoid[TwoPSetState[T]]
  ): TwoPSetState[T] = remove(op)

  def add(
    op: T
  )(implicit
    m: Monoid[TwoPSetState[T]]
  ): TwoPSetState[T] = TwoPSet[T].add(state, op)

  def contains(
    elem: T
  ): Boolean = TwoPSet[T].contains(state, elem)

  def merge(
    other: TwoPSetState[T]
  )(implicit
    b: BoundedSemilattice[TwoPSetState[T]]
  ): TwoPSetState[T] = TwoPSet[T].merge(state, other)

  def remove(
    op: T
  )(implicit
    m: Monoid[TwoPSetState[T]]
  ): TwoPSetState[T] = TwoPSet[T].remove(state, op)

}
