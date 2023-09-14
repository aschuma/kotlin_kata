package aschuma.kotlin.kata.coroutinesworkshop.hotflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class NumbersStation {
   private val scope = CoroutineScope(SupervisorJob())

   private val _numbers: MutableSharedFlow<Int> = MutableSharedFlow(replay = 5)
   val numbers: SharedFlow<Int> get() = _numbers

   fun beginBroadcasting() {
      scope.launch {
         while (true) {
            _numbers.emit(getNewNumber())
         }
      }
   }
}

suspend fun main() {
   val station = NumbersStation()
   station.beginBroadcasting()
   // Nobody listening!
   delay(5000)
   coroutineScope {
      launch {
         // Some items have been dropped
         station.numbers.collect {
            println("Received $it")
         }
      }
   }
}

suspend fun getNewNumber(): Int {
   val number = Random.nextInt()
   println("Generated $number")
   delay(500)
   return number
}
