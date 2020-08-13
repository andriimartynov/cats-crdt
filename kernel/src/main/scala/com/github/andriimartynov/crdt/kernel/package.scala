package com.github.andriimartynov.crdt

import java.util.UUID

import com.github.andriimartynov.crdt.kernel.NodeId.{ liftToNodeId, NodeId }
import com.github.andriimartynov.crdt.kernel.Timestamp.{ liftToTimestamp, Timestamp }
import supertagged.TaggedType
import supertagged.postfix._

package object kernel {
  protected object NodeIdT    extends TaggedType[String]
  protected object TimestampT extends TaggedType[Long]

  implicit val longToLongOps: Long => LongOps =
    l => new LongOps(l)

  implicit val uuidToUUIDOps: UUID => UUIDOps =
    uuid => new UUIDOps(uuid)

  final class LongOps(private val l: Long) extends AnyVal {
    def asTimestamp: Timestamp = liftToTimestamp(l)

  }

  object NodeId {
    type NodeId = NodeIdT.Type

    def liftToNodeId(str: String): NodeId =
      str @@ NodeIdT

    def create(): NodeId =
      UUID.randomUUID().asNodeId

  }

  object Timestamp {
    type Timestamp = TimestampT.Type

    def liftToTimestamp(l: Long): Timestamp =
      l @@ TimestampT

    def now(): Timestamp =
      System.currentTimeMillis().asTimestamp

  }

  final class UUIDOps(private val uuid: UUID) extends AnyVal {
    def asNodeId: NodeId = liftToNodeId(uuid.toString)

  }

}
