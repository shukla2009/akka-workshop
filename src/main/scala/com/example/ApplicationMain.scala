package com.example

import akka.actor.{Props, ActorSystem}

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")


  

  val props = Props(new PingActor[Int])
  val pingActor = system.actorOf(props, "pingActor")
  pingActor ! PingActor.PingMessage[Int](123)




}