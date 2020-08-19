package com.github.andriimartynov.crdt.instances

import cats.kernel.BoundedSemilattice
import cats.syntax.semigroup._
import com.github.andriimartynov.crdt.KeyValueStore

import scala.collection.Iterable

trait MapInstances {
  implicit def catsStdBoundedSemiLatticeForMap[K, V](implicit
    b: BoundedSemilattice[V]
  ): BoundedSemilattice[Map[K, V]] =
    new BoundedSemilattice[Map[K, V]] {
      override def combine(
        a1: Map[K, V],
        a2: Map[K, V]
      ): Map[K, V] =
        a1 ++ a2.map {
          case (k, v) =>
            k -> a1.get(k).fold(v)(_ |+| v)
        }

      override def empty: Map[K, V] = Map.empty[K, V]

    }

  implicit def catsStdKeyValueStoreForMap: KeyValueStore[Map] =
    new KeyValueStore[Map] {

      override def get[K, V](
        map: Map[K, V]
      )(
        k: K
      ): Option[V] = map.get(k)

      override def keys[K, V](
        map: Map[K, V]
      ): Iterable[K] = map.keys

      override def update[K, V](
        map: Map[K, V]
      )(
        k: K,
        v: V
      ): Map[K, V] = map.updated(k, v)

      override def values[K, V](
        map: Map[K, V]
      ): Iterable[V] = map.values

    }

}
