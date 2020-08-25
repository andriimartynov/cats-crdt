package com.github.andriimartynov.crdt.instances

import cats.instances.set.catsKernelStdHashForSet
import cats.kernel.Monoid
import cats.kernel.laws.discipline.{BoundedSemilatticeTests, EqTests}
import cats.laws.discipline.FunctorTests
import com.github.andriimartynov.crdt.instances.int._
import com.github.andriimartynov.crdt.instances.set._
import com.github.andriimartynov.crdt.kernel.laws.discipline.{GSetTests, TwoPSetTests}
import com.github.andriimartynov.crdt.kernel.sets.TwoPSet.TwoPSetState
import org.scalacheck.Arbitrary.{arbInt, arbLong, arbString}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.prop.Configuration
import org.typelevel.discipline.scalatest.FlatSpecDiscipline

class SetLawsSpec extends AnyFlatSpecLike with Configuration with FlatSpecDiscipline {

  import SetLawsSpec._

  checkAll("Set.CommutativeMonoidLaws", BoundedSemilatticeTests[Set[Int]].monoid)
  checkAll("Set.GSetLaws", GSetTests[Set[Int]].set)
  checkAll("Set.TwoPSet", TwoPSetTests[Int].set)
  checkAll("TwoPSetState.EqLaws", EqTests[TwoPSetState[Int]].eqv)
  checkAll("TwoPSetState.FunctorLaws", FunctorTests[TwoPSetState].functor[Int, String, Long])

}

object SetLawsSpec {

  implicit def arbTwoPSetStateF[T: Arbitrary](implicit
    m: Monoid[T]
  ): Arbitrary[TwoPSetState[T] => TwoPSetState[T]] =
    Arbitrary((x: TwoPSetState[T]) => x.copy(added = x.removed + m.empty, removed = x.added + m.empty))

  implicit def arbSet[T: Arbitrary]: Arbitrary[Set[T]] =
    Arbitrary(
      Gen.oneOf(
        Gen.const(Set.empty[T]),
        for {
          value  <- Arbitrary.arbitrary[T]
          value2 <- Arbitrary.arbitrary[T]
        } yield Set(value, value2)
      )
    )

  implicit def arbTwoPSetState[T: Arbitrary](implicit
    arbSet: Arbitrary[Set[T]]
  ): Arbitrary[TwoPSetState[T]] =
    Arbitrary(
      Gen.oneOf(
        Gen.const(TwoPSetState(Set.empty[T], Set.empty[T])),
        for {
          value  <- Arbitrary.arbitrary[Set[T]]
          value2 <- Arbitrary.arbitrary[Set[T]]
        } yield TwoPSetState(value, value2)
      )
    )

}
