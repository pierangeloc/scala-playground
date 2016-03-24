name := """scala-intro"""

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++= {
  val akkaV       = "2.4.1"
  val akkaStreamV = "2.0.1"
  val scalaTestV  = "2.2.4"
  val argonautV   = "6.0.4"
//  val shapelessV  = "2.2.5"
  Seq(
    "com.typesafe.akka" %% "akka-actor"                        % akkaV,
    "com.typesafe.akka" %% "akka-stream-experimental"          % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-experimental"            % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-core-experimental"       % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
    "com.chuusai"       %% "shapeless"                         % "2.2.5",
    "io.spray"          %%  "spray-json"                       % "1.3.2",
    "org.scalaz"        %% "scalaz-core"                       % "7.1.0",
    "com.typesafe.akka" %% "akka-testkit"                      % akkaV % "test",
    "org.scalatest" %% "scalatest"                             % scalaTestV % "test")
}
