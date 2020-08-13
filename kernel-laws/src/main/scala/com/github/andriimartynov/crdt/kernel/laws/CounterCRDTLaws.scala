package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.laws.IsEq
import com.github.andriimartynov.crdt.kernel.counters.CounterCRDT

trait CounterCRDTLaws[T, OpT] extends CRDTLaws[T, OpT]{
  implicit def S: CounterCRDT[T, OpT]

  def increment(x: T): IsEq[T]

  def total(x: T): IsEq[Int]

}
