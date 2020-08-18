package com.github.andriimartynov.crdt.instances

import cats.kernel.{ BoundedSemilattice, Eq }
import cats.syntax.all._
import cats.{ Functor, Show }
import com.github.andriimartynov.crdt.LWWRegister.LWWRegisterOp
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.Timestamp

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

  implicit def catsStdFunctorForLWWRegisterOp[T]: Functor[LWWRegisterOp] =
    new Functor[LWWRegisterOp] {
      override def map[A, B](fa: LWWRegisterOp[A])(f: A => B): LWWRegisterOp[B] =
        fa.copy(value = f(fa.value))
    }

  implicit def catsStdShowForLWWRegisterOp[T](implicit
    s: Show[T]
  ): Show[LWWRegisterOp[T]] =
    Show.show[LWWRegisterOp[T]](op =>
      s"LWWRegisterOp(${op.nodeId}, ${op.timestamp}, ${op.value.show})"
    )

}
