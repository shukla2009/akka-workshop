package com.avalia.akka.workshop.example.bank

import akka.actor.AbstractLoggingActor
import akka.event.LoggingReceive
import com.avalia.akka.workshop.example.bank.Account.{Deposit, Done, Failed, Withdraw}

/**
  * Created by synerzip on 12/1/16.
  */
class Account extends AbstractLoggingActor {
  var balance: BigInt = BigInt(0)

  override def receive = LoggingReceive {
    case Deposit(amount) => balance += amount
      sender ! Done
    case Withdraw(amount) if balance > amount => balance -= amount
      sender ! Done
    case _ => sender ! Failed
  }
}

object Account {

  case class Withdraw(amount: BigInt) {
    require(amount > 0)
  }

  case class Deposit(amount: BigInt) {
    require(amount > 0)
  }

  case object Failed

  case object Done

}
