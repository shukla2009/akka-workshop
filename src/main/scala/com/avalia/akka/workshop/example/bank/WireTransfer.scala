package com.avalia.akka.workshop.example.bank

import akka.actor.{AbstractLoggingActor, Actor, ActorRef}
import akka.event.LoggingReceive
import com.avalia.akka.workshop.example.bank.WireTransfer.{Done, Failed, Transfer}

/**
  * Created by synerzip on 12/1/16.
  */
class WireTransfer extends AbstractLoggingActor {


  override def receive = LoggingReceive {
    case Transfer(from, to, amount) => from ! Account.Withdraw(amount)
      context.become(awaitForWithdraw(from, to, amount, sender))
  }

  def awaitForDeposit(client: ActorRef): Actor.Receive = LoggingReceive {
    case Account.Done => {
      client ! Done
      context stop (self)
    }
  }

  def awaitForWithdraw(from: ActorRef, to: ActorRef, amount: BigInt, client: ActorRef): Receive = LoggingReceive {
    case Account.Done if sender == from => {
      to ! Account.Deposit(amount)
      context.become(awaitForDeposit(client))
    }
    case Account.Failed => {
      client ! Failed
      context stop (self)
    }
  }
}

object WireTransfer {

  case class Transfer(form: ActorRef, to: ActorRef, amount: BigInt)

  case object Done

  case object Failed

}