package com.github.andriimartynov.crdt.kernel.laws.discipline

import cats.kernel.laws.discipline.catsLawsIsEqToProp
import cats.kernel.{ BoundedSemilattice, Eq, Monoid }
import com.github.andriimartynov.crdt.kernel.laws.GSetLaws
import com.github.andriimartynov.crdt.kernel.sets.GSet
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

trait GSetTests[T] extends Laws {
  def laws: GSetLaws[T]

  def set(implicit
    arbA: Arbitrary[Set[T]],
    eqA: Eq[Set[T]]
  ): RuleSet =
    new DefaultRuleSet(
      "GSet",
      None,
      "add0"   -> forAll(laws.add0 _),
      "merge" -> forAll(laws.merge _)
    )

}

object GSetTests {
  def apply[T](implicit
    s: GSet[T],
    b: BoundedSemilattice[Set[T]],
    m: Monoid[T]
  ): GSetTests[T] =
    new GSetTests[T] {
      override def laws: GSetLaws[T] = GSetLaws[T]
    }

}
