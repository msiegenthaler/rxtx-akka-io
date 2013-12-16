package rxtxio

import akka.actor.{ Actor, ActorRef, Props, ActorSystem }
import akka.io.IO
import akka.util.ByteString
import Serial._

class Example(port: String) extends Actor {
  import context.system

  override def preStart = {
    IO(Serial) ! Open(port, 9600)
  }

  override def postStop = {
    println("Stopped")
    system.shutdown
  }

  override def receive = {
    case Opened(operator, _) =>
      println("Connected to port")
      context become open(operator)
    case CommandFailed(_, error) =>
      println(s"Could not connect to port: $error")
      context stop self
  }

  def open(operator: ActorRef): Receive = {
    case "close" =>
      operator ! Closed

    case s: String => //external input
      operator ! Write(ByteString(s))

    case Received(data) =>
      print("Received data from serial port: ")
      println(data.decodeString("UTF-8"))

    case Closed =>
      println("Serial port closed")
      context stop self
  }
}

object Example extends App {
  val port = "/dev/cu.usbserial-A6008hNp"

  val system = ActorSystem("Example")
  val actor = system.actorOf(Props(new Example(port)), "e1")

  system.awaitTermination
}