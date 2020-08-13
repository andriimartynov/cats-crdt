package com.github.andriimartynov.crdt.instances

import cats.instances.int.catsKernelStdOrderForInt
import cats.kernel.BoundedSemilattice
import com.github.andriimartynov.crdt.instances.all._
import com.github.andriimartynov.crdt.kernel.laws.discipline.LWWRegisterTests
import com.github.andriimartynov.crdt.LWWRegister.LWWRegisterOp
import com.github.andriimartynov.crdt.{ longToLongOps, NodeId }
import org.scalacheck.Arbitrary.arbInt
import org.scalacheck.Gen.oneOf
import org.scalacheck.{ Arbitrary, Gen }
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.prop.Configuration
import org.typelevel.discipline.scalatest.FlatSpecDiscipline

class RegisterLawsSpec extends AnyFlatSpecLike with Configuration with FlatSpecDiscipline {

  import RegisterLawsSpec._

  implicit val nodeId = NodeId.create()

  checkAll("Register.LWWRegisterLaws", LWWRegisterTests[Int].register)

}

object RegisterLawsSpec {
  implicit def arbRegisterOp[T: Arbitrary](implicit
    b: BoundedSemilattice[LWWRegisterOp[T]]
  ): Arbitrary[LWWRegisterOp[T]] = {
    val nodeId1 = NodeId.create()
    val nodeId2 = NodeId.create()

    val timestamp1 = (System.currentTimeMillis() - 5000L).asTimestamp
    val timestamp2 = (System.currentTimeMillis() - 2000L).asTimestamp
    val timestamp3 = (System.currentTimeMillis() - 100L).asTimestamp

    Arbitrary(
      Gen.oneOf(
        Gen.const(BoundedSemilattice[LWWRegisterOp[T]].empty),
        for {
          nodeId    <- oneOf(nodeId1, nodeId2)
          timestamp <- oneOf(timestamp1, timestamp2, timestamp3)
          value     <- Arbitrary.arbitrary[T]
        } yield LWWRegisterOp(nodeId, timestamp, value)
      )
    )
  }

}
