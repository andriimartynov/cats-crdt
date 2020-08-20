package com.github.andriimartynov.crdt.kernel.counters

import cats.kernel.Monoid
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.PNCounter.PNCounterState

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
