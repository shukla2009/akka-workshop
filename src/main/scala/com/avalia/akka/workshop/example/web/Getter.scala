package com.avalia.akka.workshop.example.web

import akka.actor.{Status, AbstractLoggingActor}
import akka.actor.Status.{Failure, Success}
import akka.pattern.pipe
import com.avalia.akka.workshop.example.web.Getter.Abort
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.concurrent.Future
import scala.collection.JavaConverters._

/**
  * Created by synerzip on 12/1/16.
  */
class Getter(url: String, depth: Int) extends AbstractLoggingActor {

  implicit val exec = context.dispatcher

  private val future: Future[String] = WebClient.get(url)

  //  future.onComplete {
  //    case Success(body) => self ! body
  //
  //    case Failure(err) => self ! Status.Failure(err)
  //  }

  future.pipeTo(self)


  //WebClient get url pipeTo self


  def getLinks(body: String): Iterator[String] = {
    val document = Jsoup.parse(body, url)
    val links = document.select("a[href]")
    for {
      link <- links.iterator().asScala
    } yield link.absUrl("href")
  }

  def stop(): Unit = {
    context.parent ! Getter.Done
    context.stop(self)

  }

  override def receive = {
    case body: String =>
      for (url <- getLinks(body))
        context.parent ! Controller.Check(url, depth)
      stop()
    case _: Status.Failure => stop()
    case Abort => stop()
  }
}

object Getter {

  case object Done

  case object Abort

}
