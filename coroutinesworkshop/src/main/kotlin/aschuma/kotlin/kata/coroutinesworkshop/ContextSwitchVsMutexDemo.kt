package aschuma.kotlin.kata.coroutinesworkshop

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

// Output:
//
// RUN 1
// Completed 100000 actions in 1231 ms
// Counter = 100000
// RUN 2
// Completed 100000 actions in 520 ms
// Counter = 100000

@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking {
   var counter = 0

   println("RUN 1")
   val counterContextSingleThreaded = newSingleThreadContext("CounterContext")
   withContext(Dispatchers.Default) {
      massiveRun {
         withContext(counterContextSingleThreaded) {
            counter++
         }
      }
   }
   println("Counter = $counter")

   println("RUN 2")
   counter = 0
   val mutex = Mutex()
   withContext(Dispatchers.Default) {
      massiveRun {
         mutex.withLock {
            counter++
         }
      }
   }
   println("Counter = $counter")
}

private suspend fun massiveRun(action: suspend () -> Unit) {
   val n = 100 // number of coroutines to launch
   val k = 1000 // times an action is repeated by each coroutine
   val time = measureTimeMillis {
      coroutineScope { // scope for coroutines
         repeat(n) {
            launch {
               repeat(k) { action() }
            }
         }
      }
   }
   println("Completed ${n * k} actions in $time ms")
}
