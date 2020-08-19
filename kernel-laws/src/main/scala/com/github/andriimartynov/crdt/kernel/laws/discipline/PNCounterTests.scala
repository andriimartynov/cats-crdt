package com.github.andriimartynov.crdt.kernel.laws.discipline

import cats.kernel.laws.discipline.catsLawsIsEqToProp
import cats.kernel.{ BoundedSemilattice, Eq }
import com.github.andriimartynov.crdt.kernel.KeyValueStore
import com.github.andriimartynov.crdt.kernel.counters.PNCounter
import com.github.andriimartynov.crdt.kernel.counters.PNCounter.PNCounterState
import com.github.andriimartynov.crdt.kernel.laws.PNCounterLaws
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

trait PNCounterTests[F[NodeId, Int]] extends Laws {
  def laws: PNCounterLaws[F]

  def counter(implicit
    arbA: Arbitrary[PNCounterState[F]],
    eqA: Eq[PNCounterState[F]],
    eqB: Eq[Int]
  ): RuleSet =
    new DefaultRuleSet(
      "GCounter",
      None,
      "add0"       -> forAll(laws.add0 _),
      "add1"       -> forAll(laws.add1 _),
      "increment0" -> forAll(laws.increment0 _),
      "decrement0" -> forAll(laws.decrement0 _),
      "merge"      -> forAll(laws.merge _),
      "total"      -> forAll(laws.total _)
    )

}

object PNCounterTests {
  def apply[F[NodeId, Int]](implicit
    c: PNCounter[F],
    b: BoundedSemilattice[PNCounterState[F]],
    k: KeyValueStore[F]
  ): PNCounterTests[F] =
    new PNCounterTests[F] { def laws: PNCounterLaws[F] = PNCounterLaws[F] }

}
