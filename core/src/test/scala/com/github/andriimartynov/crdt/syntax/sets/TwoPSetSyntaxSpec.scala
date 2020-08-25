package com.github.andriimartynov.crdt.syntax.sets

import cats.Monoid
import com.github.andriimartynov.crdt.implicits._
import com.github.andriimartynov.crdt.TwoPSet.TwoPSetState
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class TwoPSetSyntaxSpec extends AnyFlatSpec with should.Matchers {

  "A set" should "be empty" in {
    val set = Monoid[TwoPSetState[Int]].empty
    set should be(TwoPSetState(Set.empty[Int], Set.empty[Int]))
  }

  "A set" should "contain 1" in {
    val set = Monoid[TwoPSetState[Int]].empty + 1
    set should be(TwoPSetState(Set(1), Set.empty[Int]))
    set.contains(1) should be(true)
  }

  "A set" should "not contain 1" in {
    val set = Monoid[TwoPSetState[Int]].empty - 1 add 1
    set should be(TwoPSetState(Set(1), Set(1)))
    set.contains(1) should be(false)
  }

  "A set" should "contain 1,2,3" in {
    val set = Monoid[TwoPSetState[Int]].empty add 1 add 2 add 3
    set should be(TwoPSetState(Set(1, 2, 3), Set.empty[Int]))
    set.contains(1) should be(true)
    set.contains(2) should be(true)
    set.contains(3) should be(true)
  }

  "A set" should "not contain 1,2,3" in {
    val set = Monoid[TwoPSetState[Int]].empty add 1 add 2 add 3 remove 1 remove 2 remove 3
    set should be(TwoPSetState(Set(1, 2, 3), Set(1, 2, 3)))
    set.contains(1) should be(false)
    set.contains(2) should be(false)
    set.contains(3) should be(false)
  }

  "A set" should "contain 1,2,3,4" in {
    val set1 = Monoid[TwoPSetState[Int]].empty add 1 add 2 add 3 remove 3
    val set2 = Monoid[TwoPSetState[Int]].empty add 1 add 3 add 4 remove 1
    val set  = set1 merge set2
    set should be(TwoPSetState(Set(1, 2, 3, 4), Set(1, 3)))
    set.contains(1) should be(false)
    set.contains(2) should be(true)
    set.contains(3) should be(false)
    set.contains(4) should be(true)
  }

}
