package com.example

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.LoggingReceive
import com.example.PingActor.PingMessage

import scala.reflect.ClassTag

class PingActor[T:ClassTag] extends Actor with ActorLogging {

  override def receive = listen[T]

  def listen[T] = LoggingReceive{
    case msg:PingMessage[T] => log.info(msg.toString)
      context.stop(self)
  }
}

object PingActor {


  case class PingMessage[T:ClassTag](text: T)

}