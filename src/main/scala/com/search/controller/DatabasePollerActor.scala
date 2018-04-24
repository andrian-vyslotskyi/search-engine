package com.search.controller

import akka.actor.{Actor, ActorRef, Props}
import io.getquill._

import scala.concurrent.duration._
import scala.language.postfixOps

/*class DatabasePollerActor extends Actor {
  def receive = {
    case RetrieveByUUID(uuid: String) =>
      self ! RepeatAgain(sender, uuid)

    case RepeatAgain(originalSender, uuid) =>
      Database.run(uuid) match {
        case Some(x) => originalSender ! x
        case None => context.system.scheduler.scheduleOnce(50.milliseconds)
          self ! RepeatAgain(originalSender, uuid)
        }
      }
  }
}

object DatabasePollerActor {
  val props = Props[DatabasePollerActor]
}

object Database {
  val ctx = new CassandraSyncContext[Data]("kanfig")
  import ctx._

  def run(uuid: String): Option[Data] = ctx.run {
    quote {
      query[Data].filter(_.uuid == lift(uuid))
    }
  }.headOption

}

case class Data(uuid: String, data: String)


case class RetrieveByUUID(uuid: String)
case class RepeatAgain(originalSender: ActorRef, uuid: String)*/
