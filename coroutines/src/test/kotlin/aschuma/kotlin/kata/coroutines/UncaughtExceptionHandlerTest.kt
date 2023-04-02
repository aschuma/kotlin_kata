package aschuma.kotlin.kata.coroutines

import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UncaughtExceptionHandlerTest {

   @Test
   fun `without handler test`() {
      var blockExecuted = false
      thread {
         println("${Thread.currentThread().name} started")
         TimeUnit.SECONDS.sleep(1)
         val neverAssigned = "abc".toLong()
         blockExecuted = true
      }.join()

      assertFalse(blockExecuted)
   }

   @Test
   fun `with handler test`() {
      var exceptionHandled = false
      thread {
         Thread.currentThread().uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, _ ->
            println("Exception caught")
            exceptionHandled = true
         }
         println("${Thread.currentThread().name} started")
         TimeUnit.SECONDS.sleep(1)
         val neverAssigned = "abc".toLong()
      }.join()

      assertTrue(exceptionHandled)
   }
}
