package aschuma.kotlin.kata.optics

import arrow.optics.POptional
import arrow.optics.typeclasses.Index
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OpticsIndexTest {

   @Test
   fun `index basic test`() {
      // given
      val list = listOf("0", "1", "2", "3")

      // when
      val thirdListItemOptional: POptional<List<String>, List<String>, String, String> = Index.list<String>().index(3)
      val updatedList: List<String> = thirdListItemOptional.set(list, "newValue")

      // then
      assertEquals(listOf("0", "1", "2", "newValue"), updatedList)
   }
}
