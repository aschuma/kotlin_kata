package aschuma.kotlin.kata.coroutines.seq

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine

fun interface Seq<out T> {
   operator fun iterator(): Iterator<T>
}

interface SeqScope<in T> {
   suspend fun yield(value: T)
}

fun <T> seq(block: suspend SeqScope<T>.() -> Unit): Seq<T> = Seq<T> {
   val iterator = SeqBuilder<T>()
   val unpackResult: Continuation<Unit> = Continuation(
      context = EmptyCoroutineContext,
      resumeWith = Result<Unit>::getOrThrow
   )
   val newCoroutine: Continuation<Unit> = block.createCoroutine(
      receiver = iterator,
      completion = unpackResult
   )
   iterator.nextStep = newCoroutine
   iterator
}

fun <T> seqTrace(block: suspend SeqScope<T>.() -> Unit): Seq<T> = Seq<T> {
   val iterator = SeqBuilderWithTrace<T>()
   val unpackResult: Continuation<Unit> = Continuation(
      context = EmptyCoroutineContext,
      resumeWith = Result<Unit>::getOrThrow
   )
   val newCoroutine = block.createCoroutine(
      receiver = iterator,
      completion = unpackResult
   )
   iterator.nextStep = newCoroutine
   iterator
}
