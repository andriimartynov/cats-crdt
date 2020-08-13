package com.github.andriimartynov.crdt.syntax.registers

import cats.Monoid
import com.github.andriimartynov.crdt.implicits._
import com.github.andriimartynov.crdt.NodeId.NodeId
import com.github.andriimartynov.crdt.LWWRegister.LWWRegisterOp
import com.github.andriimartynov.crdt.{NodeId, longToLongOps}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class LWWRegisterSyntaxSpec extends AnyFlatSpec with should.Matchers {
  implicit val nodeId: NodeId = NodeId.create()

  "A register" should "contain 0" in {
    val register = Monoid[LWWRegisterOp[Int]].empty
    register.nodeId should be(nodeId)
    register.value should be(0)
  }

  "A register" should "contain 1" in {
    val register1 = Monoid[LWWRegisterOp[Int]].empty
    val register = register1 + 1
    register.nodeId should be(nodeId)
    register.value should be(1)
  }

  "A register" should "contain 2" in {
    val timestamp = (System.currentTimeMillis() - 10).asTimestamp
    val register1 = Monoid[LWWRegisterOp[Int]].empty add LWWRegisterOp(nodeId, timestamp, 1)
    val register2 = Monoid[LWWRegisterOp[Int]].empty + 2

    val register = register1 merge register2
    register.nodeId should be(nodeId)
    register.value should be(2)
    register.timestamp should be(register2.timestamp)
  }

}
