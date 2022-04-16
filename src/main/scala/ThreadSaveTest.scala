import com.hazelcast.core.HazelcastInstance

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.control.Breaks._

object ThreadSaveTest {
  def noLock(hazelcastInstance: HazelcastInstance)(implicit executionContext: ExecutionContext): Unit = {
    val task4Locks = hazelcastInstance.getMap[String, Int]("task4")

    val keyUnlocked = "okyshkevych"
    task4Locks.put(keyUnlocked, 0)

    val noLocksRequests = (1 to 1000).map { _ =>
      Future {
        val amount = task4Locks.get(keyUnlocked)
        Thread.sleep(10)
        task4Locks.put(keyUnlocked, amount + 1)
      }
    }
    Await.result(Future.sequence(noLocksRequests), Duration.Inf)
    println(task4Locks.get(keyUnlocked))
  }

  def pessimisticLock(hazelcastInstance: HazelcastInstance)(implicit executionContext: ExecutionContext): Unit = {
    val task4Locks = hazelcastInstance.getMap[String, Int]("task4")

    val keyPessimistic = "grandson"
    task4Locks.put(keyPessimistic, 0)

    val pessimisticLockRequests = (1 to 1000).map { _ =>
      Future {
        task4Locks.lock(keyPessimistic)
        try {
          val amount = task4Locks.get(keyPessimistic)
          Thread.sleep(10)
          task4Locks.put(keyPessimistic, amount + 1)
        } finally task4Locks.unlock(keyPessimistic)
      }
    }
    Await.result(Future.sequence(pessimisticLockRequests), Duration.Inf)
    println(task4Locks.get(keyPessimistic))
  }

  def optimisticLock(hazelcastInstance: HazelcastInstance)(implicit executionContext: ExecutionContext): Unit = {
    val task4Locks = hazelcastInstance.getMap[String, Int]("task4")

    val keyOptimistic = "grailknights"
    task4Locks.put(keyOptimistic, 0)

    val optimisticLockRequests = (1 to 1000).map { _ =>
      Future {
        breakable {
          while (true) {
            val amount = task4Locks.get(keyOptimistic)
            Thread.sleep(10)
            if (task4Locks.replace(keyOptimistic, amount, amount + 1)) break
          }
        }
      }
    }
    Await.result(Future.sequence(optimisticLockRequests), Duration.Inf)
    println(task4Locks.get(keyOptimistic))
  }
}
