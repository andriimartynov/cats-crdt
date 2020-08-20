package com.github.andriimartynov

import java.util.UUID

import com.github.andriimartynov.crdt.kernel.{ LongOps, UUIDOps }

package object crdt {

  type CounterCRDT[T]           = com.github.andriimartynov.crdt.kernel.counters.CounterCRDT[T]
  type GCounter[F[NodeId, Int]] = com.github.andriimartynov.crdt.kernel.counters.GCounter[F]
  type GSet[T]                  = com.github.andriimartynov.crdt.kernel.sets.GSet[T]
  type KeyValueStore[F[_, _]]   = com.github.andriimartynov.crdt.kernel.KeyValueStore[F]
  type LWWRegister[T]           = com.github.andriimartynov.crdt.kernel.registers.LWWRegister[T]
  type NodeId                   = com.github.andriimartynov.crdt.kernel.NodeId.NodeId
  type Timestamp                = com.github.andriimartynov.crdt.kernel.Timestamp.Timestamp
  type PNCounter[F[_, _]]       = com.github.andriimartynov.crdt.kernel.counters.PNCounter[F]

  val CounterCRDT = com.github.andriimartynov.crdt.kernel.counters.CounterCRDT
  val GCounter    = com.github.andriimartynov.crdt.kernel.counters.GCounter
  val GSet        = com.github.andriimartynov.crdt.kernel.sets.GSet
  val LWWRegister = com.github.andriimartynov.crdt.kernel.registers.LWWRegister
  val NodeId      = com.github.andriimartynov.crdt.kernel.NodeId
  val Timestamp   = com.github.andriimartynov.crdt.kernel.Timestamp
  val PNCounter   = com.github.andriimartynov.crdt.kernel.counters.PNCounter

  implicit val longToLongOps: Long => LongOps = com.github.andriimartynov.crdt.kernel.longToLongOps

  implicit val uuidToUUIDOps: UUID => UUIDOps = com.github.andriimartynov.crdt.kernel.uuidToUUIDOps

}
