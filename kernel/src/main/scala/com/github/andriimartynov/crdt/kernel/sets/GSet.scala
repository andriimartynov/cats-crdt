package com.github.andriimartynov.crdt.kernel.sets

/*
 * G-Set (Grow-only Set)
 * https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type#G-Set_(Grow-only_Set)
 * */
trait GSet[T] extends SetCRDT[T]

object GSet {
  @inline final def apply[T](implicit
    set: GSet[T]
  ): GSet[T] = set

}
