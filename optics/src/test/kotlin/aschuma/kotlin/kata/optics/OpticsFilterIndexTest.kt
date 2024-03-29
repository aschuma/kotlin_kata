package aschuma.kotlin.kata.optics

import arrow.optics.Every
import arrow.optics.PEvery
import arrow.optics.typeclasses.FilterIndex
import arrow.typeclasses.Monoid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OpticsFilterIndexTest {

   @Test
   fun `filter index basic test`() {
      //  when
      val filter: PEvery<List<Int>, List<Int>, Int, Int> = FilterIndex.list<Int>().filter { index -> index % 2 == 0 }
      val updatedList = filter
         .getAll(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))

      // then
      assertEquals(listOf(0, 2, 4, 6, 8), updatedList)
   }

   @Test
   fun `filter index mod test`() {
      val filterIndexStringByIndex: FilterIndex<List<String>, Int, String> = FilterIndex { p ->
         object : Every<List<String>, String> {
            @Deprecated("Monoid is being deprecated, use combine (A, A) -> A lambdas or method references with initial values instead.", replaceWith = ReplaceWith("foldMap(M.empty(), M::combine, source, map)", "arrow.optics.foldMap", "arrow.typeclasses.combine"))
            override fun <R> foldMap(M: Monoid<R>, source: List<String>, map: (String) -> R): R = M.run {
               source.foldIndexed(empty()) { index, acc, a -> if (p(index)) acc.combine(map(a)) else acc }
            }

            override fun modify(source: List<String>, map: (focus: String) -> String): List<String> =
               source.mapIndexed { index, a -> if (p(index)) map(a) else a }
         }
      }

      val updatedList = filterIndexStringByIndex.filter { index -> index % 2 == 0 }
         .modify(listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")) { elem -> "A${elem}O" }

      assertEquals(listOf("A0O", "1", "A2O", "3", "A4O", "5", "A6O", "7", "A8O", "9"), updatedList)
   }
}
