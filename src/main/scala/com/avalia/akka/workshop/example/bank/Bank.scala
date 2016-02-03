package com.avalia.akka.workshop.example.bank

import akka.actor.{Props, ActorRef, AbstractLoggingActor}
import akka.event.LoggingReceive
import com.avalia.akka.workshop.example.bank.Account.Deposit
import com.avalia.akka.workshop.example.bank.Bank.Init

/**
  * Created by synerzip on 12/1/16.
  */

object Bank {

  case object Init

}

class Bank extends AbstractLoggingActor {

  private val accountA: ActorRef = context.actorOf(Props[Account], "accountA")
  private val accountB: ActorRef = context.actorOf(Props[Account], "accountB")


  override def receive = LoggingReceive {
    case Init => accountA ! Deposit(100)
    case Account.Done => transfer(accountA, accountB, 50)
  }

  def transfer(from: ActorRef, to: ActorRef, amount: BigInt): Unit = {
    val wt: ActorRef = context.actorOf(Props[WireTransfer])
    wt ! WireTransfer.Transfer(from, to, amount)
    context.become(LoggingReceive {
      case WireTransfer.Done => log.debug("Transfer Complete")
      case WireTransfer.Failed => log.debug("Transfer Failed")
    })
  }

}
