package com.github.andriimartynov.crdt.kernel.laws.discipline

import cats.kernel.laws.discipline.catsLawsIsEqToProp
import cats.kernel.{ BoundedSemilattice, Eq }
import com.github.andriimartynov.crdt.kernel.KeyValueStore
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.GCounter
import com.github.andriimartynov.crdt.kernel.laws.GCounterLaws
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

trait GCounterTests[F[NodeId, Int]] extends Laws {
  def laws: GCounterLaws[F]

  def counter(implicit
    arbA: Arbitrary[F[NodeId, Int]],
    eqA: Eq[F[NodeId, Int]],
    eqB: Eq[Int]
  ): RuleSet =
    new DefaultRuleSet(
      "GCounter",
      None,
      "add0"        -> forAll(laws.add0 _),
      "add1"        -> forAll(laws.add1 _),
      "increment0" -> forAll(laws.increment0 _),
      "increment1" -> forAll(laws.increment1 _),
      "merge"      -> forAll(laws.merge _),
      "total"      -> forAll(laws.total _)
    )

}

object GCounterTests {
  def apply[F[NodeId, Int]](implicit
    c: GCounter[F],
    b: BoundedSemilattice[F[NodeId, Int]],
    k: KeyValueStore[F]
  ): GCounterTests[F] =
    new GCounterTests[F] { def laws: GCounterLaws[F] = GCounterLaws[F] }

}
