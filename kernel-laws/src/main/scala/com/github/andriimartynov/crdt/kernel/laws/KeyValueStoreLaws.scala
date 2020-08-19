package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.Monoid
import cats.kernel.laws.{ IsEq, IsEqArrow }
import com.github.andriimartynov.crdt.kernel.KeyValueStore

trait KeyValueStoreLaws[F[_, _], K, V] {
  implicit def S: KeyValueStore[F]

  implicit def MKey: Monoid[K]

  implicit def MValue: Monoid[V]

  def get(x: F[K, V]): IsEq[Boolean] = {
    val keys = S.keys(x)
    if (keys.nonEmpty) S.get(x)(keys.head).nonEmpty <-> true
    else S.values(x).isEmpty <-> true
  }

  def keys(x: F[K, V]): IsEq[Boolean] =
    (S.keys(x).size == S.values(x).size) <-> true

  def update0(x: F[K, V]): IsEq[Boolean] = {
    val updated = S.update(x)(MKey.empty, MValue.empty)
    S.get(updated)(MKey.empty).nonEmpty <-> true
  }

  def update1(x: F[K, V]): IsEq[Boolean] = {
    val updated = S.update(x)(MKey.empty, MValue.empty)
    (S.get(updated)(MKey.empty).get == MValue.empty) <-> true
  }

}

object KeyValueStoreLaws {
  def apply[F[_, _], K, V](implicit
    s: KeyValueStore[F],
    mKey: Monoid[K],
    mValue: Monoid[V]
  ): KeyValueStoreLaws[F, K, V] =
    new KeyValueStoreLaws[F, K, V] {
      def S: KeyValueStore[F] = s
      def MKey: Monoid[K]     = mKey
      def MValue: Monoid[V]   = mValue
    }

}
