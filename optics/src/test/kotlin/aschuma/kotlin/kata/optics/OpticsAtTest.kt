package aschuma.kotlin.kata.optics

import arrow.core.Option
import arrow.core.some
import arrow.optics.PLens
import arrow.optics.typeclasses.At
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OpticsAtTest {

   @Test
   fun `at basic test`() {
      // given
      val map = mapOf(
         1 to "one",
         2 to "two",
         3 to "three"
      )

      // when
      val mapAt: PLens<Map<Int, String>, Map<Int, String>, Option<String>, Option<String>> = At.map<Int, String>().at(2)
      val updatedMap = mapAt.set(map, "new value".some())

      // then
      assertEquals(
         mapOf(
            1 to "one",
            2 to "new value",
            3 to "three"
         ),
         updatedMap
      )
   }
}
