package com.github.andriimartynov.crdt.instances

import cats.kernel.{ BoundedSemilattice, Eq, Monoid }
import cats.syntax.semigroup._
import com.github.andriimartynov.crdt.CounterCRDT.CounterOp
import com.github.andriimartynov.crdt.{ KeyValueStore, PNCounter }
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.PNCounter.PNCounterState
import com.github.andriimartynov.crdt.syntax.kvs.catsSyntaxKvs

trait CounterInstancesBinCompat0 {
  implicit def catsStdBoundedSemiLatticeForPNCounterState[F[_, _]](implicit
    b: BoundedSemilattice[F[NodeId, Int]]
  ): BoundedSemilattice[PNCounterState[F]] =
    new BoundedSemilattice[PNCounterState[F]] {
      override def empty: PNCounterState[F] = PNCounterState[F](b.empty, b.empty)

      override def combine(x: PNCounterState[F], y: PNCounterState[F]): PNCounterState[F] =
        x.copy(
          inc = x.inc |+| y.inc,
          dec = x.dec |+| y.dec
        )
    }

  implicit def catsStdEqForPNCounterState[F[_, _]](implicit
    e: Eq[F[NodeId, Int]]
  ): Eq[PNCounterState[F]] =
    Eq.instance[PNCounterState[F]] {
      case (x1, x2) =>
        x1.inc == x2.inc &&
          x1.dec == x2.dec
    }

  implicit def pnCounterInstance[F[_, _]](implicit
    k: KeyValueStore[F]
  ): PNCounter[F] =
    new PNCounter[F] {
      override def add(t: PNCounter.PNCounterState[F], op: CounterOp)(implicit
        m: Monoid[PNCounter.PNCounterState[F]]
      ): PNCounter.PNCounterState[F] =
        t.copy(
          inc = t.inc.update(
            op.nodeId,
            t.inc.get(op.nodeId).fold(math.max(0, op.value))(_ + math.max(0, op.value))
          ),
          dec = t.dec.update(
            op.nodeId,
            t.dec.get(op.nodeId).fold(math.max(0, -op.value))(_ - math.min(0, op.value))
          )
        )

      def increment(
        t: PNCounterState[F]
      )(
        i: Int
      )(implicit
        m: Monoid[PNCounterState[F]],
        nodeId: NodeId
      ): PNCounterState[F] =
        add(t, CounterOp(nodeId, i))

      def decrement(
        t: PNCounterState[F]
      )(
        i: Int
      )(implicit
        m: Monoid[PNCounterState[F]],
        nodeId: NodeId
      ): PNCounterState[F] =
        add(t, CounterOp(nodeId, -i))

      override def merge(
        t1: PNCounter.PNCounterState[F],
        t2: PNCounter.PNCounterState[F]
      )(implicit b: BoundedSemilattice[PNCounter.PNCounterState[F]]): PNCounter.PNCounterState[F] =
        t1 |+| t2

      override def total(t: PNCounter.PNCounterState[F]): Int =
        t.inc.values.sum - t.dec.values.sum

    }

}
