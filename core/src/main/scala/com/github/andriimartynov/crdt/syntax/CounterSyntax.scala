package com.github.andriimartynov.crdt.syntax

import cats.kernel.{ BoundedSemilattice, Monoid }
import com.github.andriimartynov.crdt.CounterCRDT.CounterOp
import com.github.andriimartynov.crdt.{ GCounter, KeyValueStore }
import com.github.andriimartynov.crdt.NodeId.NodeId

trait CounterSyntax {
  implicit final def catsSyntaxGCounter[F[NodeId, Int]](
    kvs: F[NodeId, Int]
  ): GCounterOps[F] = new GCounterOps[F](kvs)

}

final class GCounterOps[F[NodeId, Int]](private val kvs: F[NodeId, Int]) extends AnyVal {
  def +(
    i: Int
  )(implicit
    c: GCounter[F],
    k: KeyValueStore[F],
    m: Monoid[F[NodeId, Int]],
    nodeId: NodeId
  ): F[NodeId, Int] = increment(i)

  def add(
    op: CounterOp
  )(implicit
    c: GCounter[F],
    k: KeyValueStore[F],
    m: Monoid[F[NodeId, Int]]
  ): F[NodeId, Int] = GCounter[F].add(kvs, op)

  def increment(
    i: Int
  )(implicit
    c: GCounter[F],
    k: KeyValueStore[F],
    m: Monoid[F[NodeId, Int]],
    nodeId: NodeId
  ): F[NodeId, Int] = GCounter[F].increment(kvs)(i)

  def merge(
    other: F[NodeId, Int]
  )(implicit
    b: BoundedSemilattice[F[NodeId, Int]],
    c: GCounter[F],
    k: KeyValueStore[F]
  ): F[NodeId, Int] = GCounter[F].merge(kvs, other)

  def total(implicit
    c: GCounter[F],
    k: KeyValueStore[F]
  ): Int = GCounter[F].total(kvs)

}
