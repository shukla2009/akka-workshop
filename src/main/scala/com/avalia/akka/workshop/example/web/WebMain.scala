package com.avalia.akka.workshop.example.web


import akka.actor._
import scala.concurrent.duration._

/**
  * Created by synerzip on 12/1/16.
  */
class WebMain extends AbstractLoggingActor {

  import Receptionist._

  private val recp: ActorRef = context.actorOf(Props[Receptionist], "Receptionist")

  recp ! Get("http://www.avaliatech.com")

  //context.setReceiveTimeout(10 seconds)

  override def receive = {
    case Result(url, set) =>
      println(set.toVector.sorted.mkString(s"Results for '$url':\n", "\n", "\n"))
    case Failed(url) =>
      println(s"Failed to fetch '$url'\n")
    case ReceiveTimeout =>
      context.stop(self)
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    WebClient.shutdown()
  }
}
