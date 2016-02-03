package com.avalia.akka.workshop.example.web

import java.util.concurrent.Executor

import com.ning.http.client.{Response, ListenableFuture, AsyncHttpClient}

import scala.concurrent.{Promise, Future}

/**
  * Created by synerzip on 12/1/16.
  */
object WebClient {
  val client = new AsyncHttpClient

  //  def get(url: String): String = {
  //    val response = client.prepareGet(url).execute().get()
  //    if (response.getStatusCode < 400) {
  //      response.getResponseBodyExcerpt(131072)
  //    }
  //    else throw BadStatus(response.getStatusCode)
  //  }
  def shutdown(): Unit = client.close()

  def get(url: String)(implicit exec: Executor): Future[String] = {
    val f: ListenableFuture[Response] = client.prepareGet(url).execute()
    val p = Promise[String]()
    f.addListener(new Runnable {
      override def run(): Unit = {
        val response = f.get
        if (response.getStatusCode < 400)
          p.success(response.getResponseBodyExcerpt(131072))
        else p.failure(BadStatus(response.getStatusCode))
      }
    }, exec)
    p.future
  }

  case class BadStatus(getStatusCode: Int) extends Exception

}
