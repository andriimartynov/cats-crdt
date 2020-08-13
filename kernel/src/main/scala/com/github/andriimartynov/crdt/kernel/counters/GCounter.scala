package com.github.andriimartynov.crdt.kernel.counters

import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.GCounter.GCounterOp

/*
 * G-Counter (Grow-only Counter)
 * https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type#G-Counter_(Grow-only_Counter)
 * */
trait GCounter[F[NodeId, Int]] extends CounterCRDT[F[NodeId, Int], GCounterOp]

object GCounter {
  case class GCounterOp(inc: Int, nodeId: NodeId)

  @inline final def apply[F[NodeId, Int]](implicit
    counter: GCounter[F]
  ): GCounter[F] = counter

}
