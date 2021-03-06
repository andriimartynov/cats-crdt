package com.github.andriimartynov.crdt.instances

import cats.kernel.{ BoundedSemilattice, Monoid }
import cats.syntax.semigroup._
import com.github.andriimartynov.crdt.CounterCRDT.CounterOp
import com.github.andriimartynov.crdt.{ GCounter, KeyValueStore }
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.syntax.kvs.catsSyntaxKvs

trait CounterInstances {
  implicit def gCounterInstance[F[NodeId, Int]](implicit
    k: KeyValueStore[F]
  ): GCounter[F] =
    new GCounter[F] {
      def add(
        t: F[NodeId, Int],
        op: CounterOp
      )(implicit
        m: Monoid[F[NodeId, Int]]
      ): F[NodeId, Int] =
        t.update(op.nodeId, t.get(op.nodeId).fold(math.max(0, op.value))(_ + math.max(0, op.value)))

      def increment(
        t: F[NodeId, Int]
      )(
        i: Int
      )(implicit
        m: Monoid[F[NodeId, Int]],
        nodeId: NodeId
      ): F[NodeId, Int] = add(t, CounterOp(nodeId, i))

      def merge(
        t1: F[NodeId, Int],
        t2: F[NodeId, Int]
      )(implicit
        b: BoundedSemilattice[F[NodeId, Int]]
      ): F[NodeId, Int] = t1 |+| t2

      def total(
        t: F[NodeId, Int]
      ): Int = t.values.sum

    }

}
