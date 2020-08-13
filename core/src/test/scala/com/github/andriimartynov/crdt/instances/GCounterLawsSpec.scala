package com.github.andriimartynov.crdt.instances

import cats.instances.int.catsKernelStdOrderForInt
import cats.instances.map.catsKernelStdEqForMap
import com.github.andriimartynov.crdt.implicits._
import com.github.andriimartynov.crdt.NodeId
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.laws.discipline.GCounterTests
import org.scalacheck.Gen.oneOf
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.prop.Configuration
import org.typelevel.discipline.scalatest.FlatSpecDiscipline

class GCounterLawsSpec extends AnyFlatSpecLike with Configuration with FlatSpecDiscipline {
  implicit val arbMap = GCounterLawsSpec.arbMap

  checkAll("Map.GCounterLaws", GCounterTests[Map].counter)
}

object GCounterLawsSpec {

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
        } yield Map(key -> value, key2 -> value)
      )
    )
  }

}
