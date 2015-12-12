package com.github.kevto

import akka.actor.ActorRef
import akka.event.EventBus
import scala.collection.mutable
import scala.collection.mutable._


case class PrintJob(to: String, msg: String, from: String)
case class Print(msg: String)

/**
 * Printer Event Bus.
 *
 * @author  Kevin Berendsen
 * @since   2015-12-11
 */
class PrinterEventBus extends EventBus{
  override type Event = PrintJob
  override type Classifier = String
  override type Subscriber = ActorRef // Our subscribers class type.

  var subscribers: Map[String, mutable.ListBuffer[Subscriber]] = new mutable.HashMap[String, mutable.ListBuffer[Subscriber]]()



  override def subscribe(subscriber: ActorRef, to: String): Boolean = {
    println(s"[EventBus]: ${subscriber.path.name} subscribing to ${to} ")

    if(subscribers.contains(to)) {
      subscribers.get(to).get += subscriber
    } else {
      subscribers.put(to, new mutable.ListBuffer[Subscriber]())
      subscribers.get(to).get += subscriber
    }

    true
  }



  override def publish(event: Event): Unit = {
    println(s"[EventBus]: Sending... ${event.msg}")

    if(subscribers.contains(event.to)) {
      subscribers.get(event.to).get.foreach(subscriber => {
        subscriber ! Print(s"[${event.from}] ${event.msg}")
      })
    }
  }



  override def unsubscribe(subscriber: ActorRef, from: String): Boolean = {
    println(s"[EventBus]: ${subscriber.path.name} unsubscribing from ${from}")
    //subscribers.update(from, subscribers.get(from).get.filter(subscriber => true))
    if(subscribers.contains(from)) {
      val index: Int = subscribers.get(from).get.indexOf(subscriber)
      subscribers.get(from).get.remove(index)
      true
    } else {
      false
    }
  }

  override def unsubscribe(subscriber: ActorRef): Unit = {
    // Remove the subscriber from every subscription list.
  }
}
