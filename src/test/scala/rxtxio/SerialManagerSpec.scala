package rxtxio

import akka.actor.ActorSystem
import akka.io._
import akka.testkit._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuiteLike
import Serial._

class SerialManagerSpec extends TestKit(ActorSystem("SerialManagerSpec"))
  with FunSuiteLike
  with BeforeAndAfterAll
  with ShouldMatchers
  with ImplicitSender {
  override def afterAll = system.shutdown

  test("list ports") {
    IO(Serial) ! ListPorts
    val Ports(ports) = expectMsgType[Ports]
    println("Found serial ports: " + ports.mkString(", "))
  }
}