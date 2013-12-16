# rxtx-akka-io

Akka IO library in scala for serial ports based on rxtx.

## Usage

To connect use (in an actor):

    override def preStart = IO(Serial) ! Open(port, 9600) 
    override def receive = {
      case Opened(operator, _) =>
        println("Connected to port")
        context become open(operator)
    }

The actor will be bound to the opened serial port connection using a death pact. So if the actor that opened the connection gets stopped/killed it will also close and stop the serial port connection.

The operator accepts

-   Write: To write data to the serial port (a ByteString).

-   Close: To close the serial port.

<!-- -->

    operator ! Write(ByteString(“Hello”)) // no ack

    val ackToken = new AckEvent{}
    operator ! Write(ByteString(“Hello with ack”), ackToken)

    operator ! Close

The operator will send

-   Received: Data was received no the serial port (ByteString).

-   Ack: Response to successful write.

-   Closed: When the port has been closed (i.e. as a response to Close).

messages. To handle use code like:

    case Received(data) =>
      print("Received data from serial port: ")
      println(data.toString)

    case Closed =>
      println("Serial port closed")
      context stop self

For more information see the [Akka IO Documentation][].

## Notes

-   Make sure you have the rxtx native libraries on the java library path.

-   If on Mac OS X you keep getting "gnu.io.PortInUseException: Unknown Application” try to create the /var/lock and give all users access to it.

<!-- -->

    sudo mkdir /var/lock
    sudo chmod 777 /var/lock

## References

For RXTX see: <http://rxtx.qbang.org/>

Provides serial input/output capabilities and the OS native parts.

For Akka IO see: <http://doc.akka.io/docs/akka/snapshot/scala/io.html>

Provides the basic interface definition.

  [Akka IO Documentation]: http://doc.akka.io/docs/akka/snapshot/scala/io.html
