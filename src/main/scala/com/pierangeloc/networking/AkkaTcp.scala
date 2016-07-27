package com.pierangeloc.networking

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import java.net.InetSocketAddress

object AkkaTcp extends App {

  implicit val actorSystem = ActorSystem("akka-io-exercises")
  implicit val executionContext = actorSystem.dispatcher
  //manager is an actor that handles asynchronously IO resources (sockets, channels etc)
  val manager = IO(Tcp)

  object Client {
    def props(remote: InetSocketAddress, replies: ActorRef) =
      Props(classOf[Client], remote, replies)
  }

  class Client(remote: InetSocketAddress, listener: ActorRef) extends Actor {
    import Tcp._
    import context.system

    manager ! Connect(remote)

    def receive = {
      case CommandFailed(_: Connect) =>
        listener ! "connect failed"
        context stop self

      case c @ Connected(remoteAddress, localAddress) =>
        listener ! c
        val connection = sender()
        connection ! Register(self)
    }

  }

}
