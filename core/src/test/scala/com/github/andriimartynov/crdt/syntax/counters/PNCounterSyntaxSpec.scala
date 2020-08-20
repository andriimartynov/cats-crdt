package com.github.andriimartynov.crdt.syntax.counters

import cats.kernel.Monoid
import com.github.andriimartynov.crdt.CounterCRDT.CounterOp
import com.github.andriimartynov.crdt.NodeId
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.implicits._
import com.github.andriimartynov.crdt.kernel.counters.PNCounter.PNCounterState
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PNCounterSyntaxSpec extends AnyFlatSpec with should.Matchers {

  implicit val nodeId: NodeId = NodeId.create()

  "A counter" should "be empty" in {
    val counter = Monoid[PNCounterState[Map]].empty
    counter should be(PNCounterState(Map.empty[NodeId, Int], Map.empty[NodeId, Int]))
    counter.total should be(0)
  }

  "A counter" should "be increased to one" in {
    val counter = Monoid[PNCounterState[Map]].empty + 1
    counter should be(PNCounterState(Map(nodeId -> 1), Map(nodeId -> 0)))
    counter.total should be(1)
  }

  "A counter" should "be decreased to minus one" in {
    val counter = Monoid[PNCounterState[Map]].empty - 1
    counter should be(PNCounterState(Map(nodeId -> 0), Map(nodeId -> 1)))
    counter.total should be(-1)
  }

  "A counter" should "be increased to two" in {
    val counter = Monoid[PNCounterState[Map]].empty + 1 add CounterOp(nodeId, 1)
    counter should be(PNCounterState(Map(nodeId -> 2), Map(nodeId -> 0)))
    counter.total should be(2)
  }

  "A counter" should "be decreased to minus two" in {
    val counter = Monoid[PNCounterState[Map]].empty - 1 add CounterOp(nodeId, -1)
    counter should be(PNCounterState(Map(nodeId -> 0), Map(nodeId -> 2)))
    counter.total should be(-2)
  }

  "A counter" should "be increased to three" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[PNCounterState[Map]].empty + 2 merge PNCounterState(
      Map(nodeId -> 1, nodeId2 -> 1),
      Map.empty[NodeId, Int]
    )
    counter should be(PNCounterState(Map(nodeId -> 2, nodeId2 -> 1), Map(nodeId -> 0)))
    counter.total should be(3)
  }

  "A counter" should "be decreased to minus three" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[PNCounterState[Map]].empty - 2 merge PNCounterState(
      Map.empty[NodeId, Int],
      Map(nodeId -> 2, nodeId2 -> 1)
    )
    counter should be(PNCounterState(Map(nodeId -> 0), Map(nodeId -> 2, nodeId2 -> 1)))
    counter.total should be(-3)
  }

  "A counter" should "be increased to four" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[PNCounterState[Map]].empty + 2 merge PNCounterState(
      Map(nodeId -> 2, nodeId2 -> 2),
      Map.empty[NodeId, Int]
    )
    counter should be(PNCounterState(Map(nodeId -> 2, nodeId2 -> 2), Map(nodeId -> 0)))
    counter.total should be(4)
  }

  "A counter" should "be decreased to minus four" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[PNCounterState[Map]].empty - 2 merge PNCounterState(
      Map.empty[NodeId, Int],
      Map(nodeId -> 2, nodeId2 -> 2)
    )
    counter should be(PNCounterState(Map(nodeId -> 0), Map(nodeId -> 2, nodeId2 -> 2)))
    counter.total should be(-4)
  }

  "A counter" should "be increased to five" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[PNCounterState[Map]].empty + 2 merge PNCounterState(
      Map(nodeId -> 3, nodeId2 -> 2),
      Map.empty[NodeId, Int]
    )
    counter should be(PNCounterState(Map(nodeId -> 3, nodeId2 -> 2), Map(nodeId -> 0)))
    counter.total should be(5)
  }

  "A counter" should "be decreased to minus five" in {
    val nodeId2: NodeId = NodeId.create()
    val counter         = Monoid[PNCounterState[Map]].empty - 2 merge PNCounterState(
      Map.empty[NodeId, Int],
      Map(nodeId -> 3, nodeId2 -> 2)
    )
    counter should be(PNCounterState(Map(nodeId -> 0), Map(nodeId -> 3, nodeId2 -> 2)))
    counter.total should be(-5)
  }

}
