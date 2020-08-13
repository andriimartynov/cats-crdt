lazy val `cats-crdt` = (project in file("."))
  .aggregate(
    `cats-crdt-kernel`,
    `cats-crdt-core`
  )

lazy val `cats-crdt-kernel` =
  (project in file("kernel"))
    .settings(Setup.settings)
    .settings(
      libraryDependencies += Cats.kernel,
      libraryDependencies += Supertagged.dependency
    )

lazy val `cats-crdt-kernel-laws` =
  (project in file("kernel-laws"))
    .dependsOn(`cats-crdt-kernel`)
    .settings(Setup.settings)
    .settings(
      libraryDependencies += Cats.laws,
      libraryDependencies += Supertagged.dependency
    )

lazy val `cats-crdt-core` =
  (project in file("core"))
    .dependsOn(`cats-crdt-kernel`)
    .dependsOn(`cats-crdt-kernel-laws` % Test)
    .settings(Setup.settings)
    .settings(
      libraryDependencies += Cats.core,
      libraryDependencies ++= Cats.testLawsDependencies,
      libraryDependencies += ScalaTest.dependency,
      libraryDependencies += Supertagged.dependency,
    )
