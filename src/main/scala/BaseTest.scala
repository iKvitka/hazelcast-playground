import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap

import scala.util.Random

object BaseTest {
  def dataDistributionTest(hazelcastInstance: HazelcastInstance): Unit = {
    val task3map: IMap[Int, String] = hazelcastInstance.getMap[Int, String]("task3")

    (0 to 1000).foreach(key => task3map.put(key, Random.nextString(Random.between(3, 10))))
    println(task3map.size())
  }
}
