package com.github.andriimartynov.crdt.instances

import cats.instances.int.catsKernelStdOrderForInt
import cats.instances.map.catsKernelStdEqForMap
import cats.kernel.laws.discipline.EqTests
import com.github.andriimartynov.crdt.NodeId
import com.github.andriimartynov.crdt.implicits._
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.PNCounter.PNCounterState
import com.github.andriimartynov.crdt.kernel.laws.discipline.{ GCounterTests, PNCounterTests }
import org.scalacheck.Gen.oneOf
import org.scalacheck.{ Arbitrary, Gen }
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.prop.Configuration
import org.typelevel.discipline.scalatest.FlatSpecDiscipline

class CounterLawsSpec extends AnyFlatSpecLike with Configuration with FlatSpecDiscipline {
  import CounterLawsSpec._

  checkAll("Map.GCounterLaws", GCounterTests[Map].counter)
  checkAll("Map.PNCounterLaws", PNCounterTests[Map].counter)
  checkAll("PNCounterState.EqLaws", EqTests[PNCounterState[Map]].eqv)

}

object CounterLawsSpec {
  implicit def arb: Arbitrary[PNCounterState[Map] => PNCounterState[Map]] = {
    val nodeId1 = NodeId.create()
    val nodeId2 = NodeId.create()
    Arbitrary(for {
      key   <- oneOf(nodeId1, nodeId2)
      value <- Gen.chooseNum(1, Int.MaxValue)
    } yield (x: PNCounterState[Map]) => {
      x.copy(inc = x.inc.updated(key, value))
    })

  }

  implicit def arbMap: Arbitrary[Map[NodeId, Int]] = {
    val nodeId1 = NodeId.create()
    val nodeId2 = NodeId.create()
    Arbitrary(
      Gen.oneOf(
        Gen.const(Map.empty[NodeId, Int]),
        for {
          key    <- oneOf(nodeId1, nodeId2)
          key2   <- oneOf(nodeId2, nodeId1)
          value  <- Gen.chooseNum(1, Int.MaxValue)
          value2 <- Gen.chooseNum(1, Int.MaxValue)
        } yield Map(key -> value, key2 -> value2)
      )
    )
  }

  implicit def arbMState: Arbitrary[PNCounterState[Map]] = {
    val nodeId1 = NodeId.create()
    val nodeId2 = NodeId.create()
    val nodeId3 = NodeId.create()
    val nodeId4 = NodeId.create()
    Arbitrary(
      Gen.oneOf(
        Gen.const(PNCounterState(Map.empty[NodeId, Int], Map.empty[NodeId, Int])),
        for {
          key    <- oneOf(nodeId1, nodeId2)
          key2   <- oneOf(nodeId2, nodeId1)
          key3   <- oneOf(nodeId3, nodeId4)
          key4   <- oneOf(nodeId4, nodeId3)
          value  <- Gen.chooseNum(1, Int.MaxValue)
          value2 <- Gen.chooseNum(1, Int.MaxValue)
          value3 <- Gen.chooseNum(Int.MinValue, -1)
          value4 <- Gen.chooseNum(Int.MinValue, -1)
        } yield PNCounterState(
          Map(key  -> value, key2  -> value2),
          Map(key3 -> value3, key4 -> value4)
        )
      )
    )
  }

}
