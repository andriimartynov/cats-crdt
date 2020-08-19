package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.BoundedSemilattice
import cats.kernel.laws.{ IsEq, IsEqArrow }
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.CounterCRDT.CounterOp
import com.github.andriimartynov.crdt.kernel.counters.PNCounter
import com.github.andriimartynov.crdt.kernel.counters.PNCounter.PNCounterState
import com.github.andriimartynov.crdt.kernel.{ KeyValueStore, NodeId }

trait PNCounterLaws[F[NodeId, Int]] extends CounterCRDTLaws[PNCounterState[F]] {
  implicit def S: PNCounter[F]

  implicit def B: BoundedSemilattice[PNCounterState[F]]

  implicit def K: KeyValueStore[F]

  implicit val nodeId: NodeId = NodeId.create()

  def add0(x: PNCounterState[F]): IsEq[PNCounterState[F]] =
    S.add(x, CounterOp(nodeId, 1)) <-> x.copy(
      inc = K.update(x.inc)(nodeId, K.get(x.inc)(nodeId).fold(1)(_ + 1)),
      dec = K.update(x.dec)(nodeId, K.get(x.dec)(nodeId).fold(0)(_ + 0))
    )

  def add1(x: PNCounterState[F]): IsEq[PNCounterState[F]] =
    S.add(x, CounterOp(nodeId, -1)) <-> x.copy(
      inc = K.update(x.inc)(nodeId, K.get(x.inc)(nodeId).fold(0)(_ + 0)),
      dec = K.update(x.dec)(nodeId, K.get(x.dec)(nodeId).fold(1)(_ + 1))
    )

  def increment0(x: PNCounterState[F]): IsEq[PNCounterState[F]] =
    S.increment(x)(1) <-> x.copy(
      inc = K.update(x.inc)(nodeId, K.get(x.inc)(nodeId).fold(1)(_ + 1)),
      dec = K.update(x.dec)(nodeId, K.get(x.dec)(nodeId).fold(0)(_ + 0))
    )

  def decrement0(x: PNCounterState[F]): IsEq[PNCounterState[F]] =
    S.decrement(x)(1) <-> x.copy(
      inc = K.update(x.inc)(nodeId, K.get(x.inc)(nodeId).fold(0)(_ + 0)),
      dec = K.update(x.dec)(nodeId, K.get(x.dec)(nodeId).fold(1)(_ + 1))
    )

  def merge(x1: PNCounterState[F], x2: PNCounterState[F]): IsEq[PNCounterState[F]] =
    S.merge(x1, x2) <-> x1.copy(
      inc = mergeKVS(x1.inc, x2.inc),
      dec = mergeKVS(x1.dec, x2.dec)
    )

  def total(x: PNCounterState[F]): IsEq[Int] =
    S.total(x) <-> K.values(x.inc).sum - K.values(x.dec).sum

}

object PNCounterLaws {
  def apply[F[NodeId, Int]](implicit
    c: PNCounter[F],
    b: BoundedSemilattice[PNCounterState[F]],
    k: KeyValueStore[F]
  ): PNCounterLaws[F] =
    new PNCounterLaws[F] {
      def S: PNCounter[F]                          = c
      def B: BoundedSemilattice[PNCounterState[F]] = b
      def K: KeyValueStore[F]                      = k
    }

}
