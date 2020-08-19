package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.laws.{ IsEq, IsEqArrow }
import cats.kernel.BoundedSemilattice
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.GCounter
import com.github.andriimartynov.crdt.kernel.counters.CounterCRDT.CounterOp
import com.github.andriimartynov.crdt.kernel.{ KeyValueStore, NodeId }

trait GCounterLaws[F[NodeId, Int]] extends CounterCRDTLaws[F[NodeId, Int]] {
  implicit def S: GCounter[F]

  implicit def B: BoundedSemilattice[F[NodeId, Int]]

  implicit def K: KeyValueStore[F]

  implicit val nodeId: NodeId = NodeId.create()

  def add0(x: F[NodeId, Int]): IsEq[F[NodeId, Int]] =
    S.add(x, CounterOp(nodeId, 1)) <-> K.update(x)(nodeId, K.get(x)(nodeId).fold(1)(_ + 1))

  def add1(x: F[NodeId, Int]): IsEq[F[NodeId, Int]] =
    S.add(x, CounterOp(nodeId, -1)) <-> K.update(x)(nodeId, K.get(x)(nodeId).fold(0)(_ + 0))

  def increment0(x: F[NodeId, Int]): IsEq[F[NodeId, Int]] =
    S.increment(x)(1) <-> K.update(x)(nodeId, K.get(x)(nodeId).fold(1)(_ + 1))

  def increment1(x: F[NodeId, Int]): IsEq[F[NodeId, Int]] =
    S.increment(x)(-1) <-> K.update(x)(nodeId, K.get(x)(nodeId).fold(0)(_ + 0))

  def merge(x1: F[NodeId, Int], x2: F[NodeId, Int]): IsEq[F[NodeId, Int]] =
    S.merge(x1, x2) <-> mergeKVS(x1, x2)

  def total(x: F[NodeId, Int]): IsEq[Int] =
    S.total(x) <-> K.values(x).sum

}

object GCounterLaws {
  def apply[F[NodeId, Int]](implicit
    c: GCounter[F],
    b: BoundedSemilattice[F[NodeId, Int]],
    k: KeyValueStore[F]
  ): GCounterLaws[F] =
    new GCounterLaws[F] {
      def S: GCounter[F]                        = c
      def B: BoundedSemilattice[F[NodeId, Int]] = b
      def K: KeyValueStore[F]                   = k
    }

}
