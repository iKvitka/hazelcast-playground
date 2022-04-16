import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.{ClientConfig, ClientNetworkConfig}
import com.hazelcast.config.Config

object HazelcastPlayground extends App {
  val clientConfig: ClientConfig = ClientConfig.load().setClusterName("scala-playground")
  val hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig)

  val map = hazelcastClient.getMap[String, String]("user-cash")

  map.put("user1","test")
  map.get("user1")
  map.putIfAbsent("somekey", "somevalue")
  println(map)
  println(map.size())
  map.replace("user1", "test", "newvalue")
  map.clear()
  println(map.size())
  // Shutdown this Hazelcast client
}
