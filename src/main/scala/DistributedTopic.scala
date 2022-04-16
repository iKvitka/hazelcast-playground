import com.hazelcast.core.HazelcastInstance
import com.hazelcast.topic.{ITopic, Message, MessageListener}

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DistributedTopic {
  def publishMessage(hazelcastInstance: HazelcastInstance): Future[Unit] =
    Future {
      val task6Topic: ITopic[Instant] = hazelcastInstance.getTopic[Instant]("task6")

      (1 to 5).foreach { _ =>
        task6Topic.publish(Instant.now())
        Thread.sleep(500)
      }
    }

  def consumeMessage(hazelcastInstance: HazelcastInstance): Unit = {
    val task6Topic: ITopic[Instant] = hazelcastInstance.getTopic[Instant]("task6")
    task6Topic.addMessageListener(Task6DateListener())
    println("Subscribed")
  }

  case class Task6DateListener() extends MessageListener[Instant] {
    override def onMessage(message: Message[Instant]): Unit = println(
      s"${Thread.currentThread().getName}| Received message ${message.getMessageObject}"
    )
  }
}
