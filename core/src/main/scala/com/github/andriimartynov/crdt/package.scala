package com.github.andriimartynov

import java.util.UUID

import com.github.andriimartynov.crdt.kernel.{ LongOps, UUIDOps }

package object crdt {

  type GCounter[F[NodeId, Int]] = com.github.andriimartynov.crdt.kernel.counters.GCounter[F]
  type GSet[T]                  = com.github.andriimartynov.crdt.kernel.sets.GSet[T]
  type KeyValueStore[F[_, _]]   = com.github.andriimartynov.crdt.kernel.KeyValueStore[F]
  type LWWRegister[T]           = com.github.andriimartynov.crdt.kernel.registers.LWWRegister[T]
  type NodeId                   = com.github.andriimartynov.crdt.kernel.NodeId.NodeId
  type Timestamp                = com.github.andriimartynov.crdt.kernel.Timestamp.Timestamp

  val GCounter    = com.github.andriimartynov.crdt.kernel.counters.GCounter
  val GSet        = com.github.andriimartynov.crdt.kernel.sets.GSet
  val LWWRegister = com.github.andriimartynov.crdt.kernel.registers.LWWRegister
  val NodeId      = com.github.andriimartynov.crdt.kernel.NodeId
  val Timestamp   = com.github.andriimartynov.crdt.kernel.Timestamp

  implicit val longToLongOps: Long => LongOps = com.github.andriimartynov.crdt.kernel.longToLongOps

  implicit val uuidToUUIDOps: UUID => UUIDOps = com.github.andriimartynov.crdt.kernel.uuidToUUIDOps

}
