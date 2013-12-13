package rxtxio

import akka.actor.{ Actor, ActorRef }
import akka.io.IO
import akka.util.ByteString
import Serial._

class Example(port: String) extends Actor {
  import context.system

  override def preStart = {
    IO(Serial) ! Open(port, 9600)
  }

  override def receive = {
    case Opened(operator, _) =>
      println("Connected to port")
      context become open(operator)
  }

  def open(operator: ActorRef): Receive = {
    case "close" =>
      operator ! Closed

    case s: String => //external input
      operator ! Write(ByteString(s))

    case Received(data) =>
      print("Received data from serial port: ")
      println(data.toString)

    case Closed =>
      println("Serial port closed")
      context stop self
  }

}