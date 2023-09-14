package aschuma.kotlin.kata.coroutinesworkshop.cancellationexception

import kotlinx.coroutines.*

suspend fun doSomeWorkThatMayFail() {
   println("I'm working...")
   delay(500)
}

suspend fun main() {
   val cs = CoroutineScope(Dispatchers.Default)
   val myJob = cs.launch {
      while (true) {
         try {
            doSomeWorkThatMayFail()
         } catch (e: Exception) {
            println("Didn't work this time! $e")
         }
      }
   }
   delay(2000)
   println("Enough!")
   myJob.cancelAndJoin()
}

// Output:
//
// I'm working...
// I'm working...
// I'm working...
// I'm working...
// Enough!.
// Didn't work this time! kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled; job=StandaloneCoroutine{Cancelling}@2a7ed7a9
// I'm working...
// Didn't work this time! kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled; job=StandaloneCoroutine{Cancelling}@2a7ed7a9
// I'm working...
// Didn't work this time! kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled; job=StandaloneCoroutine{Cancelling}@2a7ed7a9
// I'm working...
// Didn't work this time! kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled; job=StandaloneCoroutine{Cancelling}@2a7ed7a9
// [..]
