package aschuma.kotlin.kata.coroutinesworkshop.cancellation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

suspend fun main() = runBlocking {
   val myScope = CoroutineScope(Dispatchers.IO)

   myScope.launch {
      var x = 0
      while (true) {
         println("A: ${x++}")
         delay(50)
      }
   }

   myScope.launch {
      var y = 0
      while (true) {
         y += 2
         println("B: $y")
         if (isActive) {
            doCpuHeavyWork(100)
         } else {
            println("B: $y - cancelled")
            return@launch
         }
      }
   }

   myScope.launch {
      var y = 0
      while (true) {
         y += 4
         println("C: $y")
         ensureActive()
         doCpuHeavyWork(200)
      }
   }

   myScope.launch {
      var y = 0
      while (true) {
         y += 8
         println("D: $y")
         doCpuHeavyWork(400)
         yield() // the nicest solution - Let the other coroutines play
      }
   }

   delay(4000)
   myScope.cancel()
}

private fun doCpuHeavyWork(timeMillis: Int) {
   Thread.sleep(timeMillis.toLong())
}
