package com.github.andriimartynov.crdt.kernel.counters

import cats.kernel.Monoid
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.PNCounter.PNCounterState

/*
 * PN-Counter (Positive-Negative Counter)
 * https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type#PN-Counter_(Positive-Negative_Counter)
 * */
trait PNCounter[F[NodeId, Int]] extends CounterCRDT[PNCounterState[F]] {
  def decrement(
    t: PNCounterState[F]
  )(
    i: Int
  )(implicit
    m: Monoid[PNCounterState[F]],
    nodeId: NodeId
  ): PNCounterState[F]

}

object PNCounter {
  case class PNCounterState[F[_, _]](inc: F[NodeId, Int], dec: F[NodeId, Int])

  @inline final def apply[F[NodeId, Int]](implicit
    counter: PNCounter[F]
  ): PNCounter[F] = counter

}
