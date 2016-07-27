name := """scala-intro"""

version := "1.0"

scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += "bintray algd" at "http://dl.bintray.com/content/algd/maven"


libraryDependencies ++= {
  val akkaV       = "2.4.1"
  val akkaStreamV = "2.0.1"
  val scalaTestV  = "2.2.4"
  val argonautV   = "6.0.4"
  val http4sVersion = "0.14.0-SNAPSHOT"
  val oauth2ScalaVersion = "0.4.0"

  //  val shapelessV  = "2.2.5"
  resolvers += Resolver.sonatypeRepo("releases")

  Seq(
    "com.typesafe.akka" %% "akka-actor"                        % akkaV,
    "com.typesafe.akka" %% "akka-stream-experimental"          % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-experimental"            % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-core-experimental"       % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
    "com.basho.riak"    % "riak-client"                        % "2.0.0",
    "com.chuusai"       %% "shapeless"                         % "2.3.1",
    "io.spray"          %%  "spray-json"                       % "1.3.2",
    "org.scalaz"        %% "scalaz-core"                       % "7.1.0",
    "com.github.alexarchambault" %% "argonaut-shapeless_6.1"   % "1.1.1",
    "io.github.algd"    %% "oauth2-scala-akka-http"            % oauth2ScalaVersion,
    "com.typesafe.akka" %% "akka-testkit"                      % akkaV % "test",
    "org.scalatest" %% "scalatest"                             % scalaTestV % "test"
  )
}
