package com.github.andriimartynov.crdt.kernel.counters

import com.github.andriimartynov.crdt.kernel.NodeId.NodeId

/*
 * G-Counter (Grow-only Counter)
 * https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type#G-Counter_(Grow-only_Counter)
 * */
trait GCounter[F[NodeId, Int]] extends CounterCRDT[F[NodeId, Int]]

object GCounter {

  @inline final def apply[F[NodeId, Int]](implicit
    counter: GCounter[F]
  ): GCounter[F] = counter

}
