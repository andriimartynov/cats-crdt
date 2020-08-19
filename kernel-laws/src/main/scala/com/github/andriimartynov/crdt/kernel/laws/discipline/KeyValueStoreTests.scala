package com.github.andriimartynov.crdt.kernel.laws.discipline

import cats.kernel.instances.boolean._
import cats.kernel.laws.discipline.catsLawsIsEqToProp
import cats.kernel.{ Eq, Monoid }
import com.github.andriimartynov.crdt.kernel.KeyValueStore
import com.github.andriimartynov.crdt.kernel.laws.KeyValueStoreLaws
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

trait KeyValueStoreTests[F[_, _], K, V] extends Laws {
  def laws: KeyValueStoreLaws[F, K, V]

  def kvs(implicit
    arbA: Arbitrary[F[K, V]],
    eqA: Eq[F[K, V]]
  ): RuleSet =
    new DefaultRuleSet(
      "KeyValueStore",
      None,
      "get"     -> forAll(laws.get _),
      "keys"    -> forAll(laws.keys _),
      "update0" -> forAll(laws.update0 _),
      "update1" -> forAll(laws.update1 _)
    )

}

object KeyValueStoreTests {
  def apply[F[_, _], K, V](implicit
    s: KeyValueStore[F],
    mKey: Monoid[K],
    mValue: Monoid[V]
  ): KeyValueStoreTests[F, K, V] =
    new KeyValueStoreTests[F, K, V] {
      override def laws: KeyValueStoreLaws[F, K, V] = KeyValueStoreLaws[F, K, V]
    }

}
