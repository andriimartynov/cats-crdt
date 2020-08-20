package com.github.andriimartynov.crdt.kernel.laws.discipline

import cats.Monoid
import cats.Eq
import cats.kernel.BoundedSemilattice
import cats.kernel.instances.boolean._
import cats.kernel.laws.discipline.catsLawsIsEqToProp
import com.github.andriimartynov.crdt.kernel.laws.TwoPSetLaws
import com.github.andriimartynov.crdt.kernel.sets.TwoPSet
import com.github.andriimartynov.crdt.kernel.sets.TwoPSet.TwoPSetState
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

trait TwoPSetTests[T] extends Laws {
  def laws: TwoPSetLaws[T]

  def set(implicit
    arbA: Arbitrary[TwoPSetState[T]],
    eqA: Eq[TwoPSetState[T]]
  ): RuleSet =
    new DefaultRuleSet(
      "TwoPSet",
      None,
      "add0"      -> forAll(laws.add0 _),
      "contains0" -> forAll(laws.contains0 _),
      "contains1" -> forAll(laws.contains1 _),
      "contains2" -> forAll(laws.contains2 _),
      "remove"    -> forAll(laws.remove _),
      "merge"     -> forAll(laws.merge _)
    )
}

object TwoPSetTests {
  def apply[T](implicit
    s: TwoPSet[T],
    b: BoundedSemilattice[TwoPSetState[T]],
    m: Monoid[T]
  ): TwoPSetTests[T] =
    new TwoPSetTests[T] {
      override def laws: TwoPSetLaws[T] = TwoPSetLaws[T]
    }

}
