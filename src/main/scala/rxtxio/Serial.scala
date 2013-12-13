package rxtxio

import akka.actor._
import akka.io._

/**
 * Serial port extension based on the rxtx library for the akka IO layer.
 */
object Serial extends ExtensionId[SerialExt] with ExtensionIdProvider {
  override def lookup = Serial
  override def createExtension(system: ExtendedActorSystem): SerialExt = new SerialExt(system)

  /** Messages used by the serial IO. */
  sealed trait Message

  /** Messages that are sent to the serial port. */
  sealed trait Command extends Message

  /** Messages received from the serial port. */
  sealed trait Event extends Message

  case class CommandFailed(command: Command, reason: Throwable)

  // Communication with manager

  /** Message that is sent to the manager actor. */
  sealed trait ManagerMessage

  /** Open a serial port. Response: Opened | CommandFailed */
  case class Open(port: String, baudRate: Int) extends Command with ManagerMessage

  /** Serial port is now open. Communication is handled by the operator. */
  case class Opened(operator: ActorRef, port: String) extends Event with ManagerMessage

  /** List all available serial ports. Response: Ports | CommandFailed */
  case object ListPorts extends Command with ManagerMessage

  /** Available serial ports. */
  case class Ports(ports: Vector[String]) extends Event with ManagerMessage
}

class SerialExt(system: ExtendedActorSystem) extends akka.io.IO.Extension {
  lazy val manager = system.actorOf(Props(classOf[SerialManager]), name = "IO-SERIAL")
}