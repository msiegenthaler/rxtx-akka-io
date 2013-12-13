package rxtxio

import akka.actor._
import Serial._

/**
 *  Opens the serial port and then starts a SerialOperator to handle the communication over
 *  that port.
 */
class SerialManager extends Actor {
  override def receive = {
    case ListPorts => ???
    case Open(port, baudRate) => ???
  }
}