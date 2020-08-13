package com.github.andriimartynov.crdt.instances

import cats.kernel.{ BoundedSemilattice, Eq, Monoid }
import cats.syntax.all._
import com.github.andriimartynov.crdt.LWWRegister.LWWRegisterOp
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.{ LWWRegister, Timestamp }

trait RegisterInstances {
  implicit def catsStdBoundedSemiLatticeForLWWRegisterOp[T](implicit
    b: BoundedSemilattice[T],
    nodeId: NodeId
  ): BoundedSemilattice[LWWRegisterOp[T]] =
    new BoundedSemilattice[LWWRegisterOp[T]] {
      override def combine(
        a1: LWWRegisterOp[T],
        a2: LWWRegisterOp[T]
      ): LWWRegisterOp[T] =
        a1.timestamp match {
          case t if t > a2.timestamp => a1
          case _                     => a2
        }

      override def empty: LWWRegisterOp[T] = LWWRegisterOp(nodeId, Timestamp.now(), b.empty)

    }

  implicit def catsStdEqForLWWRegisterOp[T](implicit
    e: Eq[T]
  ): Eq[LWWRegisterOp[T]] =
    Eq.instance[LWWRegisterOp[T]] {
      case (x1, x2) =>
        x1.nodeId == x2.nodeId &&
          x1.timestamp == x2.timestamp &&
          x1.value === x2.value
    }

  implicit def lwwRegisterInstance[T]: LWWRegister[T] =
    new LWWRegister[T] {
      override def add(
        t: LWWRegisterOp[T],
        op: LWWRegisterOp[T]
      )(implicit
        m: Monoid[LWWRegisterOp[T]]
      ): LWWRegisterOp[T] = t |+| op

      override def merge(
        t1: LWWRegisterOp[T],
        t2: LWWRegisterOp[T]
      )(implicit
        b: BoundedSemilattice[LWWRegisterOp[T]]
      ): LWWRegisterOp[T] = t1 |+| t2

    }

}
