package aschuma.kotlin.kata.coroutines.seq

import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class SeqTest {

   @Test
   fun `basic seq test`() {
      val seq = seq<Int> {
         yield(1)
         yield(2)
         yield(3)
      }

      seq.iterator().asSequence().toList() shouldBeEqual listOf(1, 2, 3)
   }

   //   T: -* hasNextInt
   //   S: -* hasNext NotReady null ?
   //   B -* yield 1
   //   S: yield 1
   //   S: yield suspend 1
   //   S: *- hasNext Ready 1 true
   //   T: *- hasNextInt true
   //   T: -* nextInt
   //   S: -* next Ready 1
   //   S: *- next NotReady 1
   //   T: *- nextInt 1
   //   T: -* hasNextInt
   //   S: -* hasNext NotReady 1 ?
   //   B *-yield 1
   //   B -*yield 2
   //   S: yield 2
   //   S: yield suspend 2
   //   S: *- hasNext Ready 2 true
   //   T: *- hasNextInt true
   //   T: -* nextInt
   //   S: -* next Ready 2
   //   S: *- next NotReady 2
   //   T: *- nextInt 2
   //   T: -* hasNextInt
   //   S: -* hasNext NotReady 2 ?
   //   B *-yield 2
   //   B -*yield 3
   //   S: yield 3
   //   S: yield suspend 3
   //   S: *- hasNext Ready 3 true
   //   T: *- hasNextInt true
   //   T: -* nextInt
   //   S: -* next Ready 3
   //   S: *- next NotReady 3
   //   T: *- nextInt 3
   //   T: -* hasNextInt
   //   S: -* hasNext NotReady 3 ?
   //   B *-yield 3
   //   S: *- hasNext Done 3 false
   //   T: *- hasNextInt false

   @Test
   fun `basic seq with trace test `() {
      fun hasNext(iterator: Iterator<Int>): Boolean {
         println("T: -* hasNextInt")
         val hasNext = iterator.hasNext()
         println("T: *- hasNextInt $hasNext")
         return hasNext
      }

      val sequence = seqTrace<Int> {
         println("B -* yield 1")
         yield(1)
         println("B *-yield 1")

         println("B -*yield 2")
         yield(2)
         println("B *-yield 2")

         println("B -*yield 3")
         yield(3)
         println("B *-yield 3")
      }

      val iterator = sequence.iterator()
      val mutableList = mutableListOf<Int>()
      while (hasNext(iterator)) {
         println("T: -* nextInt")
         val nextInt = iterator.next()
         mutableList.add(nextInt)
         println("T: *- nextInt $nextInt")
      }

      mutableList.toList() shouldBeEqual listOf(1, 2, 3)
   }
}
