import sbt._

object Supertagged {
  lazy val SUPERTAGGED_VERSION: String = "2.0-RC1"

  lazy val dependency: ModuleID =
    "org.rudogma" %% "supertagged" % SUPERTAGGED_VERSION
  
}
