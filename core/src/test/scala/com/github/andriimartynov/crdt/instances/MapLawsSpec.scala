package com.github.andriimartynov.crdt.instances

import cats.instances.int.catsKernelStdOrderForInt
import cats.instances.map.catsKernelStdEqForMap
import cats.instances.string.catsKernelStdMonoidForString
import cats.kernel.laws.discipline.BoundedSemilatticeTests
import com.github.andriimartynov.crdt.instances.int._
import com.github.andriimartynov.crdt.instances.map._
import com.github.andriimartynov.crdt.kernel.laws.discipline.KeyValueStoreTests
import org.scalacheck.Arbitrary.{arbInt, arbString}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.prop.Configuration
import org.typelevel.discipline.scalatest.FlatSpecDiscipline

class MapLawsSpec extends AnyFlatSpecLike with Configuration with FlatSpecDiscipline {
  import MapLawsSpec.arbMap

  checkAll("Map.CommutativeMonoidLaws", BoundedSemilatticeTests[Map[String, Int]].monoid)
  checkAll("Map.KeyValueStoreLaws", KeyValueStoreTests[Map, String, Int].kvs)
}

object MapLawsSpec {
  implicit def arbMap[K: Arbitrary, V: Arbitrary]: Arbitrary[Map[K, V]] =
    Arbitrary(
      Gen.oneOf(
        Gen.const(Map.empty[K, V]),
        for {
          key   <- Arbitrary.arbitrary[K]
          value <- Arbitrary.arbitrary[V]
        } yield Map(key -> value)
      )
    )

}
