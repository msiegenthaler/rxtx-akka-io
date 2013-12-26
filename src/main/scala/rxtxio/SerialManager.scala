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

    case c @ Open(port, baudRate, dataBits, parity, stopBits, flowControl) =>
      Try {
        val id = CommPortIdentifier.getPortIdentifier(port)
        val data = dataBits match {
          case DataBits5 => DATABITS_5
          case DataBits6 => DATABITS_6
          case DataBits7 => DATABITS_7
          case DataBits8 => DATABITS_8
        }
        val stop = stopBits match {
          case OneStopBit => STOPBITS_1
          case OneAndHalfStopBits => STOPBITS_1_5
          case TwoStopBits => STOPBITS_2
        }
        val par = parity match {
          case NoParity => PARITY_NONE
          case EvenParity => PARITY_EVEN
          case OddParity => PARITY_ODD
          case MarkParity => PARITY_MARK
          case SpaceParity => PARITY_SPACE
        }
        val fc = flowControl match {
          case NoFlowControl => FLOWCONTROL_NONE
          case RtsFlowControl => FLOWCONTROL_RTSCTS_IN | FLOWCONTROL_RTSCTS_OUT
          case XonXoffFlowControl => FLOWCONTROL_XONXOFF_IN | FLOWCONTROL_XONXOFF_OUT
        }
        id.open(context.self.toString, 2000) match {
          case sp: SerialPort =>
            sp.setSerialPortParams(baudRate, data, stop, par)
            sp.setFlowControlMode(fc)
            sp
          case _ => throw new RuntimeException(s"$port is not a SerialPort.")
        }
      } match {
        case Success(serialPort) =>
          val operator = context.actorOf(SerialOperator.props(serialPort, sender))
          sender ! Opened(operator, port)
        case Failure(error) =>
          sender ! CommandFailed(c, error)
      }
  }
}