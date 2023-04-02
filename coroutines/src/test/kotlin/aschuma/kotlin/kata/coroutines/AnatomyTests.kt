package aschuma.kotlin.kata.coroutines

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.coroutines.*
import kotlin.test.assertTrue

class AnatomyTests {

   @Nested
   inner class Anatomy1 {

      private fun launch1(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
         block.startCoroutine(
            completion = Continuation(context = context) { result ->
               result.onFailure { exception ->
                  println("FAILURE: ${exception.message}")
                  val currentThread = Thread.currentThread()
                  currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, exception)
               }
               result.onSuccess { value ->
                  println("SUCCESS: $value")
               }
            }
         )

      @Test
      fun `launch1 basic test`() {
         // given
         var blockExecuted = false
         val context = EmptyCoroutineContext
         val block: suspend () -> Unit = {
            println("WITHIN ${Thread.currentThread().name}")
            println("HELLO WORLD!")
            blockExecuted = true
         }

         // when
         println("BEFORE ${Thread.currentThread().name}")
         launch1(context, block)
         println("AFTER ${Thread.currentThread().name}")

         // then
         assertTrue(blockExecuted)
      }

      @Test
      fun `launch1 basic test - exception`() {
         sequence<Int> { }
         // given
         var exceptionThrown = false
         val context = EmptyCoroutineContext
         val block: suspend () -> Unit = {
            println("WITHIN ${Thread.currentThread().name}")
            throw RuntimeException("HELLO WORLD!").also { exceptionThrown = true }
         }

         // when
         println("BEFORE ${Thread.currentThread().name}")
         launch1(context, block)
         println("AFTER ${Thread.currentThread().name}")

         // then
         assertTrue(exceptionThrown)
      }
   }

   @Nested
   inner class Anatomy2 {

      @Test
      fun `launch2 basic test`() {
         // given
         var blockExecuted = false
         val context = EmptyCoroutineContext
         val block: suspend () -> String = {
            println("WITHIN ${Thread.currentThread().name}")
            blockExecuted = true
            "HELLO WORLD"
         }

         // when
         println("BEFORE ${Thread.currentThread().name}")
         val answer: Either<Throwable, String> = launch2(context, block)
         println("AFTER ${Thread.currentThread().name}")

         // then
         assertTrue(blockExecuted)
         assertTrue(answer.isRight())
         assertTrue(answer.fold({ false }, { it == "HELLO WORLD" }))
      }

      @Test
      fun `launch2 basic test - exception`() {
         // given
         var exceptionThrown = false
         val context = EmptyCoroutineContext
         val block: suspend () -> String = {
            println("WITHIN ${Thread.currentThread().name}")
            throw RuntimeException("HELLO WORLD!").also { exceptionThrown = true }
         }

         // when
         println("BEFORE ${Thread.currentThread().name}")
         val answer: Either<Throwable, String> = launch2(context, block)
         println("AFTER ${Thread.currentThread().name}")

         // then
         assertTrue(exceptionThrown)
         assertTrue(answer.isLeft())
         answer.mapLeft {
            assertTrue(it is RuntimeException)
         }
      }

      @Test
      fun `launch2 basic test - nested`() {
         // given
         var exceptionThrown = false
         var blockExecuted = false
         val context = EmptyCoroutineContext
         val block: suspend () -> String = {
            println("WITHIN ${Thread.currentThread().name}")
            val innerResult = launch2 {
               println("WITHIN ${Thread.currentThread().name}")
               blockExecuted = true
               "HELLO WORLD"
            }
            println("INNER RESULT: $innerResult")
            throw RuntimeException("HELLO WORLD!").also { exceptionThrown = true }
         }

         // when
         println("BEFORE ${Thread.currentThread().name}")
         val answer: Either<Throwable, String> = launch2(context, block)
         println("AFTER ${Thread.currentThread().name}")

         // then
         assertTrue(exceptionThrown)
         assertTrue(blockExecuted)
         assertTrue(answer.isLeft())
         answer.mapLeft {
            assertTrue(it is RuntimeException)
         }
      }
   }
}

fun <T> launch2(
   context: CoroutineContext = EmptyCoroutineContext,
   block: suspend () -> T
): Either<Throwable, T> {
   val element = Launch2ContextElement<T>(RuntimeException("Not yet assigned").left())
   block.startCoroutine(
      completion = Continuation(context = context + element) { result ->
         result.onFailure { exception ->
            println("FAILURE: ${exception.message}")
            element.answer = exception.left()
            val currentThread = Thread.currentThread()
            currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, exception)
         }
         result.onSuccess { value ->
            println("SUCCESS: $value")
            element.answer = value.right()
            value.right()
         }
      }
   )
   return element.answer
}

fun <T> launch2b(
   context: CoroutineContext = EmptyCoroutineContext,
   block: () -> T
): Either<Throwable, T> {
   val element = Launch2ContextElement<T>(RuntimeException("Not yet assigned").left())
   suspend { block() }.startCoroutine(
      Continuation(context = context + element) { result ->
         result.onFailure { exception ->
            println("FAILURE: ${exception.message}")
            element.answer = exception.left()
            val currentThread = Thread.currentThread()
            currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, exception)
         }
         result.onSuccess { value ->
            println("SUCCESS: $value")
            element.answer = value.right()
            value.right()
         }
      }
   )
   return element.answer
}

class Launch2ContextElement<T>(var answer: Either<Throwable, T>) :
   AbstractCoroutineContextElement(key = Launch2ContextElement) {
   companion object Key : CoroutineContext.Key<Launch2ContextElement<*>>
}
