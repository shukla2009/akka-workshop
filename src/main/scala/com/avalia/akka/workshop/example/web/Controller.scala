package com.avalia.akka.workshop.example.web

import akka.actor.{ReceiveTimeout, Props, ActorRef, AbstractLoggingActor}
import com.avalia.akka.workshop.example.web.Controller.{Result, Check}
import scala.concurrent.duration._

/**
  * Created by synerzip on 12/1/16.
  */
object Controller {

  case class Check(url: String, depth: Int)

  case class Result(links: Set[String])

}

class Controller extends AbstractLoggingActor {
  var cache = Set.empty[String]
  var children = Set.empty[ActorRef]

  //context.setReceiveTimeout(10 seconds)

  override def receive = {
    case Check(url, depth) => if (!cache(url) && depth > 0) {
      children += context.actorOf(Props(new Getter(url, depth - 1)))
      cache += url
    }
    case Getter.Done => children -= sender
      if (children.isEmpty) context.parent ! Result(cache)
    case ReceiveTimeout =>
      context.children foreach (_ ! Getter.Abort)
  }

}
