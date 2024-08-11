package aschuma.kotlin.kata.fp

import arrow.core.prependTo
import arrow.core.tail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReverseTest {

   private tailrec fun <A> reverseImplA(aList: List<A>, acc: List<A>): List<A> = if (aList.isEmpty()) {
      acc
   } else {
      val head = aList.first()
      val tail = aList.tail()
      reverseImplA(tail, head.prependTo(acc))
   }

   fun <A> List<A>.reverseA() = reverseImplA(this, emptyList())

   fun <A> List<A>.reverseB() = fold(emptyList<A>()) { acc, a -> a.prependTo(acc) }

   infix fun <A, B> List<A>.myzipB(bList: List<B>): List<Pair<A, B>> =
      if (this.isEmpty() || bList.isEmpty()) emptyList()
      else {
         Pair(this.first(), bList.first()).prependTo(this.tail().myzipB(bList.tail()))
      }

   @Test
   fun `simple test - A`() {
      // given
      val ints = (1..10).toList()

      // when
      ints.reverseA()

      // then
      assertEquals(ints.reverseA(), listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1))
   }

   @Test
   fun `simple test - B`() {
      // given
      val ints = (1..10).toList()

      // when
      ints.reverseB()

      // then
      assertEquals(ints.reverseB(), listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1))
   }
}
