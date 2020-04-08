import bryghts._

lazy val scala213 = "2.13.1"
lazy val scala212 = "2.12.10"
lazy val scala211 = "2.11.12"

lazy val allScalaVersions = List(scala213, scala212, scala211)
lazy val nativeScalaVersions = List(scala211)


val sharedSettings = Seq(
    crossScalaVersions := {
      if (crossProjectPlatform.value == NativePlatform)
        nativeScalaVersions
      else
        allScalaVersions
    }
  , organization := "com.bryghts"
  , description := "High Priority - Give implicits a numeric priority"
  , licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
  , scalaVersion := scala211
  , name := "high-priority"
  , libraryDependencies += "org.typelevel" %% "simulacrum" % "1.0.0" % "test"
  , libraryDependencies += scalaOrganization.value % "scala-reflect" % scalaVersion.value % "provided"
  , libraryDependencies ++= {
      if (scalaVersion.value == scala213)
        Nil
      else
        Seq(compilerPlugin( "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full ) )
    }
  , scalacOptions ++= {
      if (scalaVersion.value == scala213)
        Seq( "-Ymacro-annotations" )
      else
        Nil
    }
  , publishMavenStyle := true
  , bintrayRepository := "jude"
  , bintrayOrganization := Some("bryghts")
  , git.useGitDescribe := true
  , git.formattedShaVersion := git.gitHeadCommit.value map { sha => s"v${sha.take(5).toUpperCase}" }
)

lazy val root =
  // select supported platforms
  crossProject (JSPlatform, JVMPlatform, NativePlatform)
    .crossType (CrossType.Pure) // [Pure, Full, Dummy], default: CrossType.Full
    .in (file("."))
    .settings (sharedSettings)
    .settings(sourceGenerators in Compile += Def.task {
       Generator.generate((Compile / sourceManaged).value)
     }.taskValue
     )
    .jsSettings ( // defined in sbt-scalajs-crossproject
      scalaJSUseMainModuleInitializer := true
     )
    .jvmSettings (/* ... */)

    // configure Scala-Native settings
    .nativeSettings ( // defined in sbt-scala-native
      nativeLTO := "thin"
    )

addCommandAlias("buildNative", "rootNative/nativeLink")
addCommandAlias("buildAllNative", "+rootNative/nativeLink")

addCommandAlias("buildJS", "rootJS/compile")
addCommandAlias("buildAllJS", "+rootJS/compile")

addCommandAlias("buildJVM", "rootJVM/compile")
addCommandAlias("buildAllJVM", "+rootJVM/compile")

addCommandAlias("buildAll", "buildAllNative;buildAllJS;buildAllJVM")


addCommandAlias("runNative", "rootNative/run")
addCommandAlias("runAllNative", "+rootNative/run")

addCommandAlias("runJS", "rootJS/run")
addCommandAlias("runAllJS", "+rootJS/run")

addCommandAlias("runJVM", "rootJVM/run")
addCommandAlias("runAllJVM", "+rootJVM/run")

addCommandAlias("runAll", "runAllNative;runAllJS;runAllJVM")


addCommandAlias("testNative", "rootNative/test")
addCommandAlias("testAllNative", "+rootNative/test")

addCommandAlias("testJS", "rootJS/test")
addCommandAlias("testAllJS", "+rootJS/test")

addCommandAlias("testJVM", "rootJVM/test")
addCommandAlias("testAllJVM", "+rootJVM/test")

addCommandAlias("testAll", "testAllNative;testAllJS;testAllJVM")


addCommandAlias("publishNative", "rootNative/publish")
addCommandAlias("publishAllNative", "+rootNative/publish")

addCommandAlias("publishJS", "rootJS/publish")
addCommandAlias("publishAllJS", "+rootJS/publish")

addCommandAlias("publishJVM", "rootJVM/publish")
addCommandAlias("publishAllJVM", "+rootJVM/publish")

addCommandAlias("publishAll", "publishAllNative;publishAllJS;publishAllJVM")

addCommandAlias("loop", "~;+rootNative/nativeLink;+rootJS/compile;+rootJVM/compile;runAllNative;runAllJS;runAllJVM")
