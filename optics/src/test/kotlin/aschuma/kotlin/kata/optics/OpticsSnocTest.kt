package aschuma.kotlin.kata.optics

import arrow.optics.snoc
import arrow.optics.unsnoc
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OpticsSnocTest {

   @Test
   fun `snok basic test`() {
      // given
      val list = listOf(1, 2)

      // when
      val updatedList = list snoc 3

      // then
      assertEquals(listOf(1, 2, 3), updatedList)
   }

   @Test
   fun `unsnok basic test`() {
      // given
      val list = listOf(1, 2, 3)

      // when
      val destructed: Pair<List<Int>, Int>? = list.unsnoc()

      // then
      val (listWithoutLastElement, endElement) = destructed!!
      assertEquals(listOf(1, 2), listWithoutLastElement)
      assertEquals(3, endElement)
   }

   @Test
   fun `unsnok basic empty test`() {
      // given
      val list = emptyList<Int>()

      // when
      val destructed: Pair<List<Int>, Int>? = list.unsnoc()

      // then
      assertEquals(destructed, null)
   }
}
