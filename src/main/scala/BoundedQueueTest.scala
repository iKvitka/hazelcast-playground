import com.hazelcast.collection.IQueue
import com.hazelcast.core.HazelcastInstance

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BoundedQueueTest {
  val queueName                                              = "task5BoundedQueue"
  def startWrite(hazelcastInstance: HazelcastInstance): Unit = {
    val boundedQueue: IQueue[Int] = hazelcastInstance.getQueue[Int](queueName)

    Future {
      (1 to 100).foreach { i =>
        boundedQueue.put(i)
        println(s"${Thread.currentThread().getName}| write $i to bounded queue")
        Thread.sleep(500)
      }
    }
  }

  def startRead(hazelcastInstance: HazelcastInstance): Future[Unit] = {
    val boundedQueue: IQueue[Int] = hazelcastInstance.getQueue[Int](queueName)

    Future {
      (1 to 50).foreach { _ =>
        val value = boundedQueue.take()
        println(s"${Thread.currentThread().getName}| read $value from bounded queue")
        Thread.sleep(2000)
      }
    }
  }

  def clearQueue(hazelcastInstance: HazelcastInstance): Unit = {
    val boundedQueue: IQueue[Int] = hazelcastInstance.getQueue[Int](queueName)
    while (boundedQueue.size() != 0) boundedQueue.take()
  }
}
