package com.github.andriimartynov.crdt.syntax.sets

import cats.Monoid
import com.github.andriimartynov.crdt.implicits._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class GSetSyntaxSpec extends AnyFlatSpec with should.Matchers {

  "A set" should "be empty" in {
    val set = Monoid[Set[Int]].empty
    set should be(Set.empty[Int])
  }

  "A set" should "contain 1" in {
    val set = Monoid[Set[Int]].empty add 1
    set should be(Set(1))
  }

  "A set" should "contain 1,2,3" in {
    val set = Monoid[Set[Int]].empty add 1 add 2 add 3
    set should be(Set(1, 2, 3))
  }

  "A set" should "contain 1,2,3,4" in {
    val set1 = Monoid[Set[Int]].empty add 1 add 2 add 3
    val set2 = Monoid[Set[Int]].empty add 1 add 3 add 4
    val set  = set1 merge set2
    set should be(Set(1, 2, 3, 4))
  }

}
