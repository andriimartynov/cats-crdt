import sbt.Keys._
import sbt.{Credentials, ScmInfo, URL, url}

object Setup {
  lazy val Scala211 = "2.11.12"
  lazy val Scala212 = "2.12.12"
  lazy val Scala213 = "2.13.3"

  lazy val settings = Seq(
    organization := "com.github.andriimartynov",
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    scalaVersion := Scala213,
    scalacOptions := scalacOptionsVersion(scalaVersion.value),
    startYear := Some(2020),
    crossScalaVersions := Seq(Scala211, Scala212, Scala213),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/andriimartynov/cats-crdt"),
        "scm:git:git@github.com:andriimartynov/cats-crdt.git"
      )
    ),
    pomExtra := pomExtraV,
    credentials += credentialsV
  )

  private lazy val credentialsV = Credentials(
    "GnuPG Key ID",
    "gpg",
    sys.env.getOrElse("GPG_PUBLIC_KEY", ""), // key identifier
    "ignored"                                // this field is ignored; passwords are supplied by pinentry
  )

  private lazy val pomExtraV =
    <developers>
        <developer>
          <id>andriimartynov</id>
          <name>andriimartynov</name>
          <url>https://github.com/andriimartynov</url>
        </developer>
      </developers>

  private def scalacOptionsVersion(scalaVersion: String): Seq[String] =
    if (scalaVersion == Scala213) Seq.empty
    else
      Seq(
        "-Ypartial-unification"
      )

}
