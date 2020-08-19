package com.github.andriimartynov.crdt.kernel.counters

import cats.kernel.Monoid
import com.github.andriimartynov.crdt.kernel.CRDT
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.CounterCRDT.CounterOp

trait CounterCRDT[T] extends CRDT[T, CounterOp] {
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

object CounterCRDT {
  case class CounterOp(nodeId: NodeId, value: Int)

}
