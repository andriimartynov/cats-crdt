package com.github.andriimartynov.crdt.instances

import cats.kernel.{ BoundedSemilattice, Monoid }
import cats.syntax.all._
import com.github.andriimartynov.crdt.LWWRegister
import com.github.andriimartynov.crdt.LWWRegister.LWWRegisterOp

trait RegisterInstancesBinCompat0 {
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
