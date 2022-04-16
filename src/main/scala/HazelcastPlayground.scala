import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap

import scala.util.Random

object HazelcastPlayground extends App {
  val clientConfig: ClientConfig         = ClientConfig.load().setClusterName("scala-playground")
  val hazelcastClient: HazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig)

  val task3map: IMap[Int, String] = hazelcastClient.getMap[Int, String]("task3")

  (0 to 1000).foreach(key => task3map.put(key, Random.nextString(Random.between(3, 10))))

  println(task3map.size())
}
