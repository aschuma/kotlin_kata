package aschuma.kotlin.kata.coroutines.parentchild

import io.kotest.matchers.equals.shouldBeEqual
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ParentChildTest {

   @Test
   fun `child cancel`() {
      val scope = CoroutineScope(Dispatchers.Default)

      val jobA = scope.launch {
         println("A")
         delay(1000)
      }

      val jobB = scope.launch {
         println("B")
         delay(1000)
      }

      jobB.cancel()

      jobB.isActive shouldBeEqual false
      jobB.isCancelled shouldBeEqual true

      jobA.isCancelled shouldBeEqual false
      jobA.isActive shouldBeEqual true

      scope.isActive shouldBeEqual true
   }

   @Test
   fun `parent cancel`() {
      val scope = CoroutineScope(Dispatchers.Default)

      val jobA = scope.launch {
         println("A")
         delay(1000)
      }

      val jobB = scope.launch {
         println("B")
         delay(1000)
      }

      scope.cancel()

      jobB.isActive shouldBeEqual false
      jobB.isCancelled shouldBeEqual true

      jobA.isActive shouldBeEqual false
      jobA.isCancelled shouldBeEqual true

      scope.isActive shouldBeEqual false
   }

   @Nested
   inner class Supervisor {
      @Test
      fun `supervisor - without`(): Unit = runBlocking {
         // given
         val scope = CoroutineScope(Job())
         var counter = 0

         scope.launch { counter++; throw error("launch1") }.join()
         scope.launch { counter++; throw error("launch2") }.join()

         scope.isActive shouldBeEqual false
         counter shouldBeEqual 1
      }

      @Test
      fun supervisor(): Unit = runBlocking {
         val scopeSuper = CoroutineScope(SupervisorJob())
         var counterSuper = 0

         scopeSuper.launch { counterSuper++; throw error("launch1") }.join()
         scopeSuper.launch { counterSuper++; throw error("launch2") }.join()

         scopeSuper.isActive shouldBeEqual true
         counterSuper shouldBeEqual 2
      }

      @Test
      fun `supervisor - with handler`(): Unit = runBlocking {
         var handlerExecuted = false
         val handler = CoroutineExceptionHandler { _, exception ->
            handlerExecuted = true
         }
         val scopeSuperHandler = CoroutineScope(SupervisorJob() + handler)
         var counterSuperHandler = 0

         scopeSuperHandler.launch { counterSuperHandler++; throw error("launch1") }.join()
         scopeSuperHandler.launch { counterSuperHandler++; throw error("launch2") }.join()

         scopeSuperHandler.isActive shouldBeEqual true
         counterSuperHandler shouldBeEqual 2
         handlerExecuted shouldBeEqual true
      }
   }
}
