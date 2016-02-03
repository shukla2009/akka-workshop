package com.avalia.akka.workshop.example.web

import akka.actor.{Props, ActorRef, AbstractLoggingActor}

/**
  * Created by synerzip on 13/1/16.
  */

object Receptionist {

  private case class Job(client: ActorRef, url: String)

  case class Get(url: String)

  case class Result(url: String, links: Set[String])

  case class Failed(url: String)

}

class Receptionist extends AbstractLoggingActor {

  import Receptionist._

  var reqNo = 0

  override def receive = waiting

  val waiting: Receive = {
    case Get(url) =>
      context.become(runNext(Vector(Job(sender, url))))
  }

  def running(queue: Vector[Job]): Receive = {
    case Controller.Result(links) =>
      val job = queue.head
      job.client ! Result(job.url, links)
      context.stop(sender)
      context.become(runNext(queue.tail))
    case Get(url) =>
      context.become(enqueueJob(queue, Job(sender, url)))
  }

  def runNext(queue: Vector[Job]): Receive = {
    reqNo += 1
    if (queue.isEmpty) waiting
    else {
      val controller = context.actorOf(Props[Controller], s"c$reqNo")
      controller ! Controller.Check(queue.head.url, 2)
      running(queue)
    }
  }

  def enqueueJob(queue: Vector[Job], job: Job): Receive = {
    if (queue.size > 3) {
      sender ! Failed(job.url)
      running(queue)
    } else running(queue :+ job)
  }


}