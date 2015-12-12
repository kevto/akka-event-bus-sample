package com.github.kevto

import akka.actor.ActorSystem

/**
 * Program.
 *
 * @author  Kevin Berendsen
 * @since   2015-12-11
 */
object Program extends App {
  val system = ActorSystem("eventbus")
  val eventBus = new PrinterEventBus
  val printerOne = system.actorOf(PrinterActor.props, "printer-1")
  val printerTwo = system.actorOf(PrinterActor.props, "printer-2")

  eventBus.subscribe(printerOne, "prints")
  eventBus.subscribe(printerTwo, "prints")
  eventBus.subscribe(printerTwo, "printer-2")

  eventBus.publish(PrintJob("prints", "First message hello", "Kevin"))
  eventBus.publish(PrintJob("printer-2", "This is pretty cool", "Kevin"))

  eventBus.unsubscribe(printerOne, "prints")
  eventBus.publish(PrintJob("prints", "printer-1 unsubscribed...", "Kevin"))
}
