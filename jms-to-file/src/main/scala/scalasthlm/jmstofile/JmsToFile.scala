
package scalasthlm.jmstofile

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.alpakka.jms.{JmsSinkSettings, JmsSourceSettings}
import akka.stream.alpakka.jms.scaladsl.{JmsSink, JmsSource}
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.broker.BrokerService

import scala.concurrent.Future

object JmsToFile extends App {

  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher

  def main(): Unit = {
    val activeMqBroker = startActiveMqBroker()

    val connectionFactory = new ActiveMQConnectionFactory("vm://localhost?create=false")

    val jmsSink: Sink[String, NotUsed] = JmsSink(
      JmsSinkSettings(connectionFactory).withQueue("test")
    )
    val in = List("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k")
    Source(in).runWith(jmsSink)

    // format: off
    // #jms-to-file
    val jmsSource: Source[String, NotUsed] = JmsSource.textSource(
      JmsSourceSettings(connectionFactory).withBufferSize(10).withQueue("test")
    )

    val fileSink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(Paths.get("target/out"))

    jmsSource
      .map(ByteString(_))
      .runWith(fileSink)
    // #jms-to-file
    // format: on

    Thread.sleep(5000)
    actorSystem.terminate().onComplete { f =>
      shutdownActiveMq(activeMqBroker)
    }
  }

  private def startActiveMqBroker() = {
    val broker = new BrokerService()
    broker.setBrokerName("localhost")
    broker.setUseJmx(false)
    broker.start()
    broker
  }

  private def shutdownActiveMq(broker: BrokerService) = {
    broker.stop()
    broker.waitUntilStopped()
  }

  main()

}
