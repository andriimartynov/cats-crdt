package com.github.andriimartynov.crdt.instances

import cats.kernel.BoundedSemilattice

trait IntInstances {
  implicit val catsStdBoundedSemiLatticeForInt: BoundedSemilattice[Int] =
    new BoundedSemilattice[Int] {
      override def combine(a1: Int, a2: Int): Int =
        a1 max a2

      override val empty: Int = 0

    }

}
