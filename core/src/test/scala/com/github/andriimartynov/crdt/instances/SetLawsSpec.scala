package com.github.andriimartynov.crdt.instances

import cats.instances.set.catsKernelStdHashForSet
import cats.kernel.laws.discipline.BoundedSemilatticeTests
import com.github.andriimartynov.crdt.instances.int._
import com.github.andriimartynov.crdt.instances.set._
import com.github.andriimartynov.crdt.kernel.laws.discipline.GSetTests
import org.scalacheck.Arbitrary.arbInt
import org.scalacheck.{ Arbitrary, Gen }
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.prop.Configuration
import org.typelevel.discipline.scalatest.FlatSpecDiscipline

class SetLawsSpec extends AnyFlatSpecLike with Configuration with FlatSpecDiscipline {

  import SetLawsSpec._

  checkAll("Set.CommutativeMonoidLaws", BoundedSemilatticeTests[Set[Int]].monoid)
  checkAll("Set.GSetLaws", GSetTests[Set[Int]].set)
}

object SetLawsSpec {

  implicit def arbSet[T: Arbitrary]: Arbitrary[Set[T]] =
    Arbitrary(
      Gen.oneOf(
        Gen.const(Set.empty[T]),
        for {
          value <- Arbitrary.arbitrary[T]
          value2 <- Arbitrary.arbitrary[T]
        } yield Set(value, value2)
      )
    )
}
