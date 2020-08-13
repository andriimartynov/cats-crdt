package com.github.andriimartynov.crdt.kernel

import cats.kernel.{ BoundedSemilattice, Monoid }

trait CRDT[T, OpT] {
  def add(
    t: T,
    op: OpT
  )(implicit
    m: Monoid[T]
  ): T

  def merge(
    t1: T,
    t2: T
  )(implicit
    b: BoundedSemilattice[T]
  ): T

}
