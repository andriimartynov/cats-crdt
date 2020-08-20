package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.BoundedSemilattice
import cats.kernel.laws.IsEq
import com.github.andriimartynov.crdt.kernel.CRDT

trait CRDTLaws[T, OpT] {
  implicit def S: CRDT[T, OpT]

  implicit def B: BoundedSemilattice[T]

  def add0(x: T): IsEq[T]

  def merge(x1: T, x2: T): IsEq[T]

}
