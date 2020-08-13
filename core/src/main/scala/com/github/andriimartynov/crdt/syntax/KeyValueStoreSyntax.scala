package com.github.andriimartynov.crdt.syntax

import com.github.andriimartynov.crdt.KeyValueStore

trait KeyValueStoreSyntax {
  implicit final def catsSyntaxKvs[F[_, _], K, V](
    kvs: F[K, V]
  ): KvsOps[F, K, V] = new KvsOps(kvs)
}

final class KvsOps[F[_, _], K, V](private val f: F[K, V]) extends AnyVal {
  def get(
    key: K
  )(implicit
    kvs: KeyValueStore[F]
  ): Option[V] = kvs.get(f)(key)

  def update(
    key: K,
    value: V
  )(implicit
    kvs: KeyValueStore[F]
  ): F[K, V] = kvs.update(f)(key, value)

  def values(implicit
    kvs: KeyValueStore[F]
  ): Iterable[V] = kvs.values(f)

}
