package com.github.andriimartynov.crdt.syntax

import cats.kernel.{ BoundedSemilattice, Monoid }
import com.github.andriimartynov.crdt.GSet
import com.github.andriimartynov.crdt.instances.set._

trait SetSyntax {
  implicit final def catsSyntaxGSet[T](
    set: Set[T]
  ): GSetOps[T] = new GSetOps(set)

}

final class GSetOps[T](private val set: Set[T]) extends AnyVal {
  def +(
    op: T
  )(implicit
    m: Monoid[Set[T]]
  ): Set[T] = add(op)

  def add(
    op: T
  )(implicit
    m: Monoid[Set[T]]
  ): Set[T] = GSet[T].add(set, op)

  def merge(
    other: Set[T]
  )(implicit
    b: BoundedSemilattice[Set[T]]
  ): Set[T] = GSet[T].merge(set, other)

}
