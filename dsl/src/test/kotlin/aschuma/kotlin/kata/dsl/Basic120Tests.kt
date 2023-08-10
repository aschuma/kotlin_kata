package aschuma.kotlin.kata.dsl

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.mapOrAccumulate
import arrow.core.nel
import arrow.core.nonEmptyListOf
import arrow.core.raise.Raise
import arrow.core.raise.RaiseAccumulate
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.zipOrAccumulate
import arrow.core.right
import arrow.core.traverse
import arrow.core.zip
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@Suppress("UNUSED_VARIABLE")
class Basic120Tests {

   object TestError

   @Test
   fun `traverse replacement test`() {
      // given/when
      fun one(): Either<String, Int> = Either.Right(1)

      val old: Either<String, List<Int>> = listOf(1, 2, 3).traverse { one() }

      val new: Either<String, List<Int>> = either {
         listOf(1, 2, 3).map { one().bind() }
      }

      // then
      assertEquals(old, new)
      assertEquals(new, generateSequence { 1 }.take(3).toList().right())
   }

   @Test
   fun `zip replacement test`() {
      // given/when
      fun one(): Either<String, Int> = Either.Right(1)

      val old: Either<String, Int> = one().zip(one()) { x, y -> x + y }

      val new: Either<String, Int> = either {
         val self: Raise<String> = this

         one().bind() + one().bind()
      }

      // then
      assertEquals(old, new)
      assertEquals(new, 2.right())
   }

   @Test
   fun `ensure test`() {
      // given/when
      val either: Either<TestError, Unit> = either {
         ensure(true) { raise(TestError) }
      }

      // then
      assertEquals(either, Unit.right())

      // given/when
      val eitherLeft: Either<TestError, Unit> = either {
         ensure(false) { raise(TestError) }
      }

      // then
      assertEquals(eitherLeft, TestError.left())
   }

   @Test
   fun `ensureNotNull test`() {
      // given/when
      val either: Either<TestError, Unit> = either {
         ensureNotNull("SOMETHING") { raise(TestError) }
      }

      // then
      assertEquals(either, Unit.right())

      // given/when
      val eitherLeft: Either<TestError, Unit> = either {
         ensureNotNull(null) { raise(TestError) }
      }

      // then
      assertEquals(eitherLeft, TestError.left())
   }

   @Test
   fun `raise test`() {
      // given/when
      val eitherLeft: Either<TestError, Unit> = either {
         raise(TestError)
      }

      // given/when
      assertEquals(eitherLeft, TestError.left())
   }

   @Test
   fun `mapOrAccumulate test - collect the left's if any, otherwise lift the right's`() {
      // given
      fun one(): Either<String, Int> = "error-1".left()
      fun two(): Either<NonEmptyList<String>, Int> = nonEmptyListOf("error-2", "error-3").left()

      // when/then
      listOf(TestError.left(), 2.right()).mapOrAccumulate {
         it.bind()
      } shouldBe TestError.nel().left()

      listOf(1, 2).mapOrAccumulate {
         one().bind()
      } shouldBe nonEmptyListOf("error-1", "error-1").left()

      listOf(1, 2).mapOrAccumulate {
         // two().bind()
         two().bindNel()
      } shouldBe nonEmptyListOf("error-2", "error-3", "error-2", "error-3").left()

      listOf(1, 2).mapOrAccumulate {
         val self: RaiseAccumulate<Nothing> = this
         42.right().bind()
      } shouldBe listOf(42, 42).right()
   }

   @Test
   fun `zipOrAccumulate test`() {
      // given
      fun one(): Either<String, Int> = "error-1".left()
      fun two(): Either<NonEmptyList<String>, Int> = nonEmptyListOf("error-2", "error-3").left()

      // when/then
      either<NonEmptyList<String>, Int> {
         val self: Raise<NonEmptyList<String>> = this

         zipOrAccumulate(action1 = {
            val self: RaiseAccumulate<String> = this
            one().bind()
         }, action2 = {
               val selfInner: RaiseAccumulate<String> = this
               two().bindNel()
            }) { x, y -> x + y }
      } shouldBe nonEmptyListOf("error-1", "error-2", "error-3").left()

      // when/then
      either<NonEmptyList<String>, Int> {
         zipOrAccumulate(action1 = {
            1
         }, action2 = {
               2
            }) { x, y -> x + y }
      } shouldBe 3.right()
   }

   @Test
   fun `fold map`() {
      // given
      fun booleanToString(b: Boolean): String = if (b) "IS TRUE! :)" else "IS FALSE.... :("
      val e1: Either<String, Boolean> = false.right()

      // when/then
      e1.fold(
         ifLeft = { "" },
         ifRight = ::booleanToString
      ) shouldBe "IS FALSE.... :(" // empty is not found
   }
}
