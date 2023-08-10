package aschuma.kotlin.kata.dsl

import arrow.core.None
import arrow.core.raise.NullableRaise
import arrow.core.raise.OptionRaise
import arrow.core.raise.nullable
import arrow.core.raise.option
import arrow.core.raise.result
import arrow.core.some
import arrow.core.toOption
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@Suppress("UNUSED_VARIABLE", "UNREACHABLE_CODE")
class ComprehensionTest {

   @Test
   fun `learning option comprehension`() {
      assertEquals(
         expected = "ab".some(),
         actual = option {
            val self: OptionRaise = this
            "a".some().bind() + "b".some().bind()
         }
      )

      assertEquals(
         expected = None,
         actual = option {
            (null as String?).toOption().bind() + "b".some().bind()
            44
         }
      )

      assertEquals(
         expected = None,
         actual = option {
            val a: String = raise(None)
            33
         }
      )

      assertEquals(
         expected = None,
         actual = option {
            ensure(false)
            22
         }
      )

      val nullIntValue: Int? = null
      val actual = option {
         val someValue: Int = ensureNotNull(nullIntValue)
         11
      }
      assertEquals(
         expected = None,
         actual = actual
      )
   }

   @Test
   fun `learning null comprehension`() {
      assertEquals(
         expected = null,
         actual = nullable {
            val self: NullableRaise = this
            val nonNullString: String = (null as String?).bind()
            "a".bind()
         }
      )

      assertEquals(
         expected = "a",
         actual = nullable {
            "a".bind()
         }
      )
   }

   @Test
   fun `learning result comprehension`() {
      assertEquals(
         expected = Result.success("a"),
         actual = result {
            Result.success("a").bind()
         }
      )

      val strResult: Result<String> = Result.failure(RuntimeException())
      assertEquals(
         expected = strResult,
         actual = result<String> {
            val first = Result.success("a").bind()
            val second = strResult.bind()
            second
         }
      )

      val exception = RuntimeException()
      assertEquals(
         expected = Result.failure<String>(exception),
         actual = result {
            val str: String = raise(exception)
            str
         }
      )
   }
}
