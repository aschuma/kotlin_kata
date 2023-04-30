package aschuma.kotlin.kata.coroutines.seq

import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SeqBuilder<T> : SeqScope<T>, Iterator<T> {

   enum class State {
      NotReady, Ready, Done
   }

   private var state: State = State.NotReady
   private var nextValue: T? = null
   var nextStep: Continuation<Unit>? = null

   override fun next(): T {
      return when (state) {
         State.NotReady -> if (hasNext()) next() else error("STATE MISMATCH NOT_READY")
         State.Ready -> {
            state = State.NotReady
            nextValue as T
         }

         State.Done -> error("STATE MISMATCH DONE")
      }
   }

   override fun hasNext(): Boolean {
      while (true) {
         when (state) {
            State.NotReady -> {
               state = State.Done
               nextStep!!.resume(Unit)
            }

            State.Done -> return false
            State.Ready -> return true
         }
      }
   }

   override suspend fun yield(value: T) {
      nextValue = value
      state = State.Ready
      suspendCoroutine { c ->
         nextStep = c
         COROUTINE_SUSPENDED
      }
   }
}
