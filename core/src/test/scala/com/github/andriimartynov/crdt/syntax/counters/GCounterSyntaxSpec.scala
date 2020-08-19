package com.github.andriimartynov.crdt.syntax.counters

import cats.kernel.Monoid
import com.github.andriimartynov.crdt.implicits._
import com.github.andriimartynov.crdt.NodeId
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.CounterCRDT.CounterOp
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class GCounterSyntaxSpec extends AnyFlatSpec with should.Matchers {

  implicit val nodeId: NodeId = NodeId.create()

  "A counter" should "be empty" in {
    val counter = Monoid[Map[NodeId, Int]].empty
    counter should be(Map.empty)
    counter.total should be(0)
  }

  "A counter" should "be increased to one" in {
    val counter = Monoid[Map[NodeId, Int]].empty + 1
    counter should be(Map(nodeId -> 1))
    counter.total should be(1)
  }

  "A counter" should "be increased to two" in {
    val counter = Monoid[Map[NodeId, Int]].empty + 1 add CounterOp(nodeId, 1)
    counter should be(Map(nodeId -> 2))
    counter.total should be(2)
  }

  "A counter" should "be increased to three" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[Map[NodeId, Int]].empty + 2 merge Map(nodeId -> 1, nodeId2 -> 1)
    counter should be(Map(nodeId -> 2, nodeId2 -> 1))
    counter.total should be(3)
  }

  "A counter" should "be increased to four" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[Map[NodeId, Int]].empty + 2 merge Map(nodeId -> 2, nodeId2 -> 2)
    counter should be(Map(nodeId -> 2, nodeId2 -> 2))
    counter.total should be(4)
  }

  "A counter" should "be increased to five" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[Map[NodeId, Int]].empty + 2 merge Map(nodeId -> 3, nodeId2 -> 2)
    counter should be(Map(nodeId -> 3, nodeId2 -> 2))
    counter.total should be(5)
  }

}
