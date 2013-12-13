package rxtxio

import akka.actor.ExtendedActorSystem
import akka.io.IO
import akka.actor.Props

class SerialExt(system: ExtendedActorSystem) extends IO.Extension {
  lazy val manager = system.actorOf(Props(classOf[SerialManager]), name = "rxtx-scala-serial")
}