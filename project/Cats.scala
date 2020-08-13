import sbt._

object Cats {
  lazy val CATS_VERSION: String = "2.0.0"

  lazy val core: ModuleID =
    "org.typelevel" %% "cats-core" % CATS_VERSION

  lazy val kernel: ModuleID =
    "org.typelevel" %% "cats-kernel" % CATS_VERSION

  lazy val laws: ModuleID =
    "org.typelevel" %% "cats-laws" % CATS_VERSION

  lazy val testLawsDependencies: Seq[ModuleID] = Seq(
    laws % Test,
    "org.typelevel" %% "discipline-scalatest" % CATS_VERSION % Test,
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.3" % Test
  )

}
