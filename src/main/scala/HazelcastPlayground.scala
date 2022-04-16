import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig
import com.hazelcast.core.HazelcastInstance

import scala.concurrent.ExecutionContext.Implicits.global

object HazelcastPlayground extends App {
  val clientConfig: ClientConfig           = ClientConfig.load().setClusterName("scala-playground")
  val hazelcastInstance: HazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig)

//  BaseTest.dataDistributionTest(hazelcastInstance)

  time(ThreadSaveTest.noLock(hazelcastInstance))
  time(ThreadSaveTest.pessimisticLock(hazelcastInstance))
  time(ThreadSaveTest.optimisticLock(hazelcastInstance))

  def time[T](f: => T): Unit = {
    val start = System.nanoTime()
    f
    val end   = System.nanoTime()
    println(s"Time taken: ${(end - start) / 1000 / 1000} ms")
  }
}
