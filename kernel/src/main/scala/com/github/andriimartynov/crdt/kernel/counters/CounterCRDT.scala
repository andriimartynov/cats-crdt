package com.github.andriimartynov.crdt.kernel.counters

import cats.kernel.Monoid
import com.github.andriimartynov.crdt.kernel.CRDT
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId

trait CounterCRDT[T, OpT] extends CRDT[T, OpT] {
  def increment(
    t: T
  )(
    i: Int
  )(implicit
    m: Monoid[T],
    nodeId: NodeId
  ): T

  def total(t: T): Int

}
