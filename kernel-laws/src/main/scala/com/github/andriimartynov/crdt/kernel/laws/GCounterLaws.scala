package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.laws.{IsEq, IsEqArrow}
import cats.kernel.{BoundedSemilattice, Monoid}
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.GCounter
import com.github.andriimartynov.crdt.kernel.counters.GCounter.GCounterOp
import com.github.andriimartynov.crdt.kernel.{KeyValueStore, NodeId}

trait GCounterLaws[F[NodeId, Int]] extends CounterCRDTLaws[F[NodeId, Int], GCounterOp] {
  implicit def S: GCounter[F]

  implicit def B: BoundedSemilattice[F[NodeId, Int]]

  implicit def K: KeyValueStore[F]

  implicit val nodeId: NodeId = NodeId.create()

  def add(x: F[NodeId, Int]): IsEq[F[NodeId, Int]] =
    S.add(x, GCounterOp(1, nodeId)) <-> K.update(x)(nodeId, K.get(x)(nodeId).fold(1)(_ max 1))

  def increment(x: F[NodeId, Int]): IsEq[F[NodeId, Int]] =
    S.increment(x)(1) <-> K.update(x)(nodeId, K.get(x)(nodeId).fold(1)(_ max 1))

  def merge(x1: F[NodeId, Int], x2: F[NodeId, Int]): IsEq[F[NodeId, Int]] =
    S.merge(x1, x2) <-> K
      .keys(x2)
      .foldLeft(x1) {
        case (map, key) =>
          K.update(map)(
            key,
            K.get(map)(key)
              .fold(K.get(x2)(key).get)(_ max K.get(x2)(key).get)
          )
      }

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
