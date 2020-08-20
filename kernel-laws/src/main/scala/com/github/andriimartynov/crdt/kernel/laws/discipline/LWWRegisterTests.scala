package com.github.andriimartynov.crdt.kernel.laws.discipline

import cats.kernel.laws.discipline.catsLawsIsEqToProp
import cats.kernel.{ BoundedSemilattice, Eq, Monoid }
import com.github.andriimartynov.crdt.kernel.laws.LWWRegisterLaws
import com.github.andriimartynov.crdt.kernel.registers.LWWRegister
import com.github.andriimartynov.crdt.kernel.registers.LWWRegister.LWWRegisterOp
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

trait LWWRegisterTests[T] extends Laws {
  def laws: LWWRegisterLaws[T]

  def register(implicit
    arbA: Arbitrary[LWWRegisterOp[T]],
    eqA: Eq[LWWRegisterOp[T]]
  ): RuleSet =
    new DefaultRuleSet(
      "LWWRegister",
      None,
      "add0"   -> forAll(laws.add0 _),
      "add1"   -> forAll(laws.add1 _),
      "merge" -> forAll(laws.merge _)
    )

}

object LWWRegisterTests {
  def apply[T](implicit
    s: LWWRegister[T],
    b: BoundedSemilattice[LWWRegisterOp[T]],
    m: Monoid[T]
  ): LWWRegisterTests[T] =
    new LWWRegisterTests[T] {
      override def laws: LWWRegisterLaws[T] = LWWRegisterLaws[T]
    }

}
