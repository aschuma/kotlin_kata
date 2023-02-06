package aschuma.kotlin.kata.optics

import arrow.optics.cons
import arrow.optics.uncons
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OpticsConsTest {

   @Test
   fun `cons basic test`() {
      // given
      val list = listOf(2, 3)

      // when
      val updatedList = 1 cons list

      // then
      assertEquals(listOf(1, 2, 3), updatedList)
   }

   @Test
   fun `uncons basic test`() {
      // given
      val list = listOf(1, 2, 3)

      // when
      val destructed: Pair<Int, List<Int>>? = list.uncons()

      // then
      val (head, tail) = destructed!!
      assertEquals(listOf(2, 3), tail)
      assertEquals(1, head)
   }

   @Test
   fun `uncons basic empty test`() {
      // given
      val list = emptyList<Int>()

      // when
      val destructed: Pair<Int, List<Int>>? = list.uncons()

      // then
      assertEquals(destructed, null)
   }
}
