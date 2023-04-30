package aschuma.kotlin.kata.coroutines.seq

import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SeqBuilderWithTrace<T> : SeqScope<T>, Iterator<T> {

   enum class State {
      NotReady, Ready, Done
   }

   private var state: State = State.NotReady // enum NotReady, Ready, Done
   private var nextValue: T? = null
   var nextStep: Continuation<Unit>? = null

   override fun next(): T {
      println("S: -* next $state $nextValue")
      return when (state) {
         State.NotReady -> if (hasNext()) next() else error("STATE MISMATCH NOT_READY")
         State.Ready -> {
            state = State.NotReady
            nextValue as T
         }

         State.Done -> error("STATE MISMATCH DONE")
      }.also {
         println("S: *- next $state $nextValue")
      }
   }

   override fun hasNext(): Boolean {
      println("S: -* hasNext $state $nextValue ?")
      val answer = doHasNext()
      println("S: *- hasNext $state $nextValue $answer")
      return answer
   }

   private fun doHasNext(): Boolean {
      while (true) {
         when (state) {
            State.NotReady -> {
               state = State.Done
               nextStep!!.resume(Unit) // ctrl back to block
            }

            State.Done -> return false
            State.Ready -> return true
         }
      }
   }

   override suspend fun yield(value: T) {
      println("S: yield " + value)
      nextValue = value
      state = State.Ready
      suspendCoroutine { c ->
         println("S: yield suspend " + value)
         nextStep = c
         COROUTINE_SUSPENDED
      }
   }
}
