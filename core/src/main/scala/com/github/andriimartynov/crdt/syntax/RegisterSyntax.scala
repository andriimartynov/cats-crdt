package com.github.andriimartynov.crdt.syntax

import cats.kernel.{ BoundedSemilattice, Monoid }
import com.github.andriimartynov.crdt.LWWRegister.LWWRegisterOp
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.{ LWWRegister, Timestamp }
import com.github.andriimartynov.crdt.instances.register._

trait RegisterSyntax {
  implicit final def catsSyntaxLWWRegister[T](
    state: LWWRegisterOp[T]
  ): LWWRegisterOps[T] = new LWWRegisterOps(state)

}

final class LWWRegisterOps[T](private val state: LWWRegisterOp[T]) extends AnyVal {
  def +(
    t: T
  )(implicit
    m: Monoid[LWWRegisterOp[T]],
    nodeId: NodeId
  ): LWWRegisterOp[T] = add(LWWRegisterOp(nodeId, Timestamp.now(), t))

  def add(
    op: LWWRegisterOp[T]
  )(implicit
    m: Monoid[LWWRegisterOp[T]]
  ): LWWRegisterOp[T] = LWWRegister[T].add(state, op)

  def merge(
    other: LWWRegisterOp[T]
  )(implicit
    b: BoundedSemilattice[LWWRegisterOp[T]]
  ): LWWRegisterOp[T] = LWWRegister[T].merge(state, other)

}
