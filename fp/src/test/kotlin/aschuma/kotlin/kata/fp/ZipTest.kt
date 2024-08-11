package aschuma.kotlin.kata.fp

import arrow.core.prependTo
import arrow.core.tail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ZipTest {

   private tailrec fun <A, B> myzipImpl(aList: List<A>, bList: List<B>, acc:List<Pair<A,B>> = mutableListOf()): List<Pair<A, B>> =
      if (aList.isEmpty() || bList.isEmpty())
         acc
      else {
         myzipImpl(aList.tail(), bList.tail(),  acc + (Pair(aList.first(), bList.first())))
      }

   infix fun <A, B> List<A>.myzipA(bList: List<B>): List<Pair<A, B>> = myzipImpl(this, bList)

   infix fun <A, B> List<A>.myzipB(bList: List<B>): List<Pair<A, B>> =
      if (this.isEmpty() || bList.isEmpty())
         emptyList()
      else {
         Pair(this.first(), bList.first()).prependTo(this.tail().myzipB( bList.tail()))
      }

   @Test
   fun `simple test - A`() {
      // given
      val ints = (1..5).toList()
      val chars = ('a'..'z').toList()

      // when
      val pairsA = ints myzipA chars
      val pairsB = chars myzipA ints

      // then
      assertEquals(
         pairsA, listOf(
            Pair(1, 'a'), Pair(2, 'b'), Pair(3, 'c'), Pair(4, 'd'), Pair(5, 'e')
         )
      )

      assertEquals(
         pairsB, listOf(
            Pair('a', 1), Pair('b', 2), Pair('c', 3), Pair('d', 4), Pair('e', 5)
         )
      )
   }

   @Test
   fun `simple test - B `() {
      // given
      val ints = (1..5).toList()
      val chars = ('a'..'z').toList()

      // when
      val pairsA = ints myzipB chars
      val pairsB = chars myzipB ints

      // then
      assertEquals(
         pairsA, listOf(
            Pair(1, 'a'), Pair(2, 'b'), Pair(3, 'c'), Pair(4, 'd'), Pair(5, 'e')
         )
      )

      assertEquals(
         pairsB, listOf(
            Pair('a', 1), Pair('b', 2), Pair('c', 3), Pair('d', 4), Pair('e', 5)
         )
      )
   }
}
