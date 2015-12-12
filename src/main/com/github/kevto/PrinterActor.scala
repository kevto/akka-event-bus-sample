package com.github.kevto

import akka.actor.Actor.Receive
import akka.actor.{Props, Actor, ActorLogging}

/**
 * Printer Actor
 *
 * @author  Kevin Berendsen
 * @since   2015-12-11
 */
class PrinterActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case job: Print =>
      println(s"[${self.path.name}]: ${job.msg}")
  }
}

object PrinterActor {
  val props = Props[PrinterActor]
}
