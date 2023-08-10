package aschuma.kotlin.kata.dsl

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.raise.NullableRaise
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.fold
import arrow.core.raise.nullable
import arrow.core.right
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

@Suppress("UNUSED_VARIABLE")
class FunctionalErrorTest2 {

   data class User(val id: Long)
   data class UserNotFound(val message: String = "Problem")

   private fun User.isValid(): Either<UserNotFound, Unit> = either {
      ensure(id > 0) { UserNotFound("User without a valid id: $id") }
   }

   private fun Raise<UserNotFound>.isValid(user: User): User {
      ensure(user.id > 0) { UserNotFound("User without a valid id: ${user.id}") }
      return user
   }

   @Test
   fun `learning fp error handling 1_2_0 - part 1`() {
      // given/when/then
      User(-1).isValid() shouldBe UserNotFound("User without a valid id: -1").left()

      fold(block = {
         val self: Raise<UserNotFound> = this
         isValid(User(1))
      }, recover = { _: UserNotFound -> fail("Unexpected 1") }, transform = { user: User -> user.id shouldBe 1 })
   }

   @Test
   fun `learning fp error handling 1_2_0 - part 2`() {
      // given/when/then
      fun process(user: User?): Either<UserNotFound, Long> = either {
         ensureNotNull(user) { UserNotFound("Cannot process null user") }
         user.id // smart-casted to non-null
      }
      process(null) shouldBe UserNotFound("Cannot process null user").left()

      // given/when/then
      fun Raise<UserNotFound>.process(user: User?): Long {
         ensureNotNull(user) { UserNotFound("Cannot process null user") }
         return user.id // smart-casted to non-null
      }
      fold({ this.process(User(1L)) }, { _: UserNotFound -> fail("Unexpected ") }, { i: Long -> i shouldBe 1L })
   }

   @Test
   fun `learning fp error handling 1_2_0 - nested`() {
      fun problematic(n: Int): Either<UserNotFound, Int?> = either {
         val eitherSelf = this

         val intOrNull: Int? = nullable {
            val nullableSelf: NullableRaise = this
            when {
               n < 0 -> eitherSelf.raise(UserNotFound())
               n == 0 -> nullableSelf.raise(null)
               n == 1 -> raise(null)
               else -> n
            }
         }
         intOrNull
      }

      problematic(-1) shouldBeEqual UserNotFound().left()
      problematic(0) shouldBeEqual null.right()
      problematic(1) shouldBeEqual null.right()
      problematic(2) shouldBeEqual 2.right()
   }

   @Test
   fun `learning fp error handling 1_2_0 - recover`() {
      arrow.core.raise.recover({
         raise(UserNotFound())
      }) { _: UserNotFound -> null } shouldBe null

      UserNotFound().left().getOrElse { null } shouldBe null
   }
}
