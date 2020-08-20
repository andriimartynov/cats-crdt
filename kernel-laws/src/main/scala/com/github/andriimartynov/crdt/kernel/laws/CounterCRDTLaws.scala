package com.github.andriimartynov.crdt.kernel.laws

import cats.kernel.laws.IsEq
import com.github.andriimartynov.crdt.kernel.KeyValueStore
import com.github.andriimartynov.crdt.kernel.NodeId.NodeId
import com.github.andriimartynov.crdt.kernel.counters.CounterCRDT
import com.github.andriimartynov.crdt.kernel.counters.CounterCRDT.CounterOp

trait CounterCRDTLaws[T] extends CRDTLaws[T, CounterOp] {
  implicit def S: CounterCRDT[T]

  def increment0(x: T): IsEq[T]

  def total(x: T): IsEq[Int]

  protected def mergeKVS[F[_, _]](
                                   x1: F[NodeId, Int],
                                   x2: F[NodeId, Int]
                                 )(implicit K: KeyValueStore[F]): F[NodeId, Int] =
    K.keys(x2)
    .foldLeft(x1) {
      case (map, key) =>
        K.update(map)(
          key,
          K.get(map)(key)
            .fold(K.get(x2)(key).get)(_ max K.get(x2)(key).get)
        )
    }

}
