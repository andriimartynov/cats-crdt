package com.github.andriimartynov.crdt.kernel.sets

import com.github.andriimartynov.crdt.kernel.CRDT

/*
 * G-Set (Grow-only Set)
 * https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type#G-Set_(Grow-only_Set)
 * */
trait GSet[T] extends CRDT[Set[T], T]

object GSet {
  @inline final def apply[T](implicit
    set: GSet[T]
  ): GSet[T] = set

}
