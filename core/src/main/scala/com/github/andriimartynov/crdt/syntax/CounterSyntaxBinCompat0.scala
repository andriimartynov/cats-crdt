package com.github.andriimartynov.crdt.syntax

import cats.kernel.{ BoundedSemilattice, Monoid }
import com.github.andriimartynov.crdt.{ GCounter, KeyValueStore, PNCounter }
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.CounterCRDT.CounterOp
import com.github.andriimartynov.crdt.kernel.counters.PNCounter.PNCounterState

trait CounterSyntaxBinCompat0 {
  implicit final def catsSyntaxPNCounter[F[NodeId, Int]](
    state: PNCounterState[F]
  ): PNCounterOps[F] = new PNCounterOps[F](state)
}

final class PNCounterOps[F[NodeId, Int]](private val state: PNCounterState[F]) extends AnyVal {
  def +(
    i: Int
  )(implicit
    c: PNCounter[F],
    k: KeyValueStore[F],
    m: Monoid[PNCounterState[F]],
    nodeId: NodeId
  ): PNCounterState[F] = increment(i)

  def -(
    i: Int
  )(implicit
    c: PNCounter[F],
    k: KeyValueStore[F],
    m: Monoid[PNCounterState[F]],
    nodeId: NodeId
  ): PNCounterState[F] = decrement(i)

  def add(
    op: CounterOp
  )(implicit
    c: PNCounter[F],
    k: KeyValueStore[F],
    m: Monoid[PNCounterState[F]]
  ): PNCounterState[F] = PNCounter[F].add(state, op)

  def increment(
    i: Int
  )(implicit
    c: PNCounter[F],
    k: KeyValueStore[F],
    m: Monoid[PNCounterState[F]],
    nodeId: NodeId
  ): PNCounterState[F] = PNCounter[F].increment(state)(i)

  def decrement(
    i: Int
  )(implicit
    c: PNCounter[F],
    k: KeyValueStore[F],
    m: Monoid[PNCounterState[F]],
    nodeId: NodeId
  ): PNCounterState[F] = PNCounter[F].decrement(state)(i)

  def merge(
    other: PNCounterState[F]
  )(implicit
    b: BoundedSemilattice[PNCounterState[F]],
    c: PNCounter[F],
    k: KeyValueStore[F]
  ): PNCounterState[F] = PNCounter[F].merge(state, other)

  def total(implicit
    c: PNCounter[F],
    k: KeyValueStore[F]
  ): Int = PNCounter[F].total(state)

}
