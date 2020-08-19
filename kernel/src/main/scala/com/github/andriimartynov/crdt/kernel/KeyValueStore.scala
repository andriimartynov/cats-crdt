package com.github.andriimartynov.crdt.kernel

import scala.collection.Iterable

trait KeyValueStore[F[_, _]] {
  def get[K, V](f: F[K, V])(k: K): Option[V]
  def keys[K, V](f: F[K, V]): Iterable[K]
  def update[K, V](f: F[K, V])(k: K, v: V): F[K, V]
  def values[K, V](f: F[K, V]): Iterable[V]

}
