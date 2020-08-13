package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.laws.{ IsEq, IsEqArrow }
import cats.kernel.{ BoundedSemilattice, Monoid }
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.registers.LWWRegister
import com.github.andriimartynov.crdt.kernel.registers.LWWRegister.LWWRegisterOp
import com.github.andriimartynov.crdt.kernel.{ longToLongOps, NodeId }

trait LWWRegisterLaws[T] extends CRDTLaws[LWWRegisterOp[T], LWWRegisterOp[T]] {
  implicit def S: LWWRegister[T]

  implicit def B: BoundedSemilattice[LWWRegisterOp[T]]

  implicit def M: Monoid[T]

  implicit val nodeId: NodeId = NodeId.create()

  override def add(x: LWWRegisterOp[T]): IsEq[LWWRegisterOp[T]] = {
    val op    = LWWRegisterOp(nodeId, (System.currentTimeMillis() - 1000L).asTimestamp, M.empty)
    val added = S.add(x, op)
    if (x.timestamp > op.timestamp) added <-> x
    else added <-> op
  }

  override def merge(x1: LWWRegisterOp[T], x2: LWWRegisterOp[T]): IsEq[LWWRegisterOp[T]] = {
    val merged = S.merge(x1, x2)
    if (x1.timestamp > x2.timestamp) merged <-> x1
    else merged <-> x2
  }

}

object LWWRegisterLaws {
  def apply[T](implicit
    s: LWWRegister[T],
    b: BoundedSemilattice[LWWRegisterOp[T]],
    m: Monoid[T]
  ): LWWRegisterLaws[T] =
    new LWWRegisterLaws[T] {
      def S: LWWRegister[T]                       = s
      def B: BoundedSemilattice[LWWRegisterOp[T]] = b
      def M: Monoid[T]                            = m
    }

}
