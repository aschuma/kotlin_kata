package aschuma.kotlin.kata.coroutinesworkshop.cancellation

import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
   coroutineScope {
      launch {
         while (true) {
            println("X")
            delay(500)
         }
      }
      launch {
         while (true) {
            println("Y")
            delay(500)
         }
      }
      delay(2000)
      cancel()
   }
}

// Output:
//
// X
// Y
// X
// Y
// Y
// X
// Y
// X
// Exception in thread "main" kotlinx.coroutines.JobCancellationException: ScopeCoroutine was cancelled; job=ScopeCoroutine{Cancelled}@1517365b
//
