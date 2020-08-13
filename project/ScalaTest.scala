import sbt._

object ScalaTest {
  lazy val SCALATEST_VERSION = "3.2.1"

  def dependency: ModuleID =
    "org.scalatest" %% "scalatest" % SCALATEST_VERSION % Test

}
