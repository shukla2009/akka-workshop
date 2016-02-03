package com.avalia.akka.workshop.example.bank

import akka.actor.{ActorRef, Props, ActorSystem}
import com.avalia.akka.workshop.example.bank.Account.Deposit

/**
  * Created by synerzip on 12/1/16.
  */
object BankExampleMain extends App {

  private val system: ActorSystem = ActorSystem.create("BankExample")
  system.actorOf(Props[Bank], "ABCBank") ! Bank.Init


}
