package com.github.andriimartynov.crdt.kernel.registers

import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.Timestamp.Timestamp
import com.github.andriimartynov.crdt.kernel.registers.LWWRegister.LWWRegisterOp

/*
 * Last-Write-Wins (LWW-Register)
 * */
trait LWWRegister[T] extends RegisterCRDT[LWWRegisterOp[T], LWWRegisterOp[T]]

object LWWRegister {
  case class LWWRegisterOp[T](nodeId: NodeId, timestamp: Timestamp, value: T)

  @inline final def apply[T](implicit
    register: LWWRegister[T]
  ): LWWRegister[T] = register

}
