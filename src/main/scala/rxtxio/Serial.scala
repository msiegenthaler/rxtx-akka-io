package rxtxio

import akka.actor._
import akka.io._

/** Serial port extension based on the rxtx library for the akka IO layer. */
object Serial extends ExtensionId[SerialExt] with ExtensionIdProvider {
  override def lookup = Serial
  override def createExtension(system: ExtendedActorSystem): SerialExt = new SerialExt(system)

  /** Messages used by the serial IO. */
  sealed trait Message

  /** Messages that are sent to the serial port. */
  sealed trait Command extends Message

  /** Messages received from the serial port. */
  sealed trait Event extends Message
}

class SerialExt(system: ExtendedActorSystem) extends akka.io.IO.Extension {
  lazy val manager = system.actorOf(Props(classOf[SerialManager]), name = "IO-SERIAL")
}