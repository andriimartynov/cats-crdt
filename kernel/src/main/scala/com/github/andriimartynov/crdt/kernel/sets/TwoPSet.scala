package com.github.andriimartynov.crdt.kernel.sets

import cats.kernel.Monoid
import com.github.andriimartynov.crdt.kernel.CRDT
import com.github.andriimartynov.crdt.kernel.sets.TwoPSet.TwoPSetState

/*
 * 2P-Set (Two-Phase Set)
 * https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type#2P-Set_(Two-Phase_Set)
 * */
trait TwoPSet[T] extends CRDT[TwoPSetState[T], T] {

  def contains(
    t: TwoPSetState[T],
    elem: T
  ): Boolean

  def remove(
    t: TwoPSetState[T],
    op: T
  )(implicit
    m: Monoid[TwoPSetState[T]]
  ): TwoPSetState[T]

}

object TwoPSet {
  case class TwoPSetState[T](added: Set[T], removed: Set[T])

  @inline final def apply[T](implicit
    set: TwoPSet[T]
  ): TwoPSet[T] = set

}
