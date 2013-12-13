package rxtxio

import scala.collection.JavaConversions._
import akka.actor._
import Serial._
import gnu.io._
import SerialPort._
import scala.util.{ Try, Success, Failure }

/**
 *  Opens the serial port and then starts a SerialOperator to handle the communication over
 *  that port.
 */
private[rxtxio] class SerialManager extends Actor {
  override def receive = {
    case ListPorts =>
      val ids = CommPortIdentifier.getPortIdentifiers.asInstanceOf[java.util.Enumeration[CommPortIdentifier]]
      val ports = ids.map(_.getName).toVector
      sender ! Ports(ports)

    case c @ Open(port, baudRate) =>
      Try {
        val id = CommPortIdentifier.getPortIdentifier(port)
        id.open(context.self.toString, 2000) match {
          case sp: SerialPort =>
            sp.setSerialPortParams(baudRate, DATABITS_8, STOPBITS_1, PARITY_NONE)
            sp
          case _ => throw new RuntimeException(s"$port is not a SerialPort.")
        }
      } match {
        case Success(serialPort) =>
          val operator = context.actorOf(Props(new SerialOperator(serialPort)))
          sender ! Opened(operator, port)
        case Failure(error) =>
          sender ! CommandFailed(c, error)
      }
  }
}