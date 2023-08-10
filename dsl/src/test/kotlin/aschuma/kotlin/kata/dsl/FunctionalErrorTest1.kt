package aschuma.kotlin.kata.dsl

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.fold
import arrow.core.right
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

@Suppress("UNUSED_VARIABLE")
class FunctionalErrorTest1 {

   object UserNotFound
   data class User(val id: Long)

   private fun user(): User = User(1)

   @Test
   fun `learning fp error handling 1_2_0 - right`() {
      // given/when
      val userE: Either<UserNotFound, User> = either { user() }

      // then
      userE shouldBe user().right()
      when (userE) {
         is Either.Left -> fail("Unexpected 1")
         is Either.Right -> userE.value shouldBe User(1)
      }

      // given/when/then
      fold(
         block = { user() },
         recover = { _: UserNotFound -> fail("Unexpected 2") },
         transform = { u: User -> u shouldBe User(1) }
      )
   }

   @Test
   fun `learning fp error handling 1_2_0 - left`() {
      // given/when
      val userE: Either<UserNotFound, User> = either { raise(UserNotFound) }

      // then
      userE shouldBe UserNotFound.left()
      when (userE) {
         is Either.Left -> userE.value shouldBe UserNotFound
         is Either.Right -> fail("Unexpected 3")
      }

      // given/when/then
      fold(
         block = { raise(UserNotFound) },
         recover = { u: UserNotFound -> u },
         transform = { u: User -> fail("Unexpected 4") }
      )
   }
}
