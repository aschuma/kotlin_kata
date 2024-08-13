package aschuma.kotlin.kata.fp

import arrow.core.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OperatorTest {

   private val funs: List<(Int) -> Int> = listOf({ x: Int -> x + 1 }, { x: Int -> x * 2 }, { x: Int -> x - 3 })

   infix operator fun List<(Int) -> Int>.invoke(arg: Int) = map { f -> f(arg) }

   infix operator fun <A, B, C> ((B) -> C).times(g: (A) -> B): (A) -> C = { x -> this(g(x)) }

   infix operator fun <A, B, C> ((B) -> C).times(fList: List<(A) -> B>): List<(A) -> C> = fList.map { f -> this * f }

   operator fun ((Int) -> Int).unaryPlus(): (List<Int>) -> List<Int> = { xs: List<Int> -> xs.map(this) }

   infix fun lift(f: (Int) -> Int): (List<Int>) -> List<Int> = f.unaryPlus()

   infix fun <A,B> A.pipe(f: (A) -> B) : B = f(this)

   @Test
   fun `simple test - list f app - A`() {
      // when
      val vals = funs.map { f ->
         f(3)
      }

      // then
      assertEquals(vals, listOf(4, 6, 0))
   }

   @Test
   fun `simple test - list f app - B`() {
      // when
      val vals = funs.asSequence().zip(generateSequence { 3 }).map { (f, z) -> f(z) }.toList()

      // then
      assertEquals(vals, listOf(4, 6, 0))
   }

   @Test
   fun `simple test - list f fold - C`() {
      // when
      val vals: List<Int> = funs.fold(emptyList()) { acc, f ->
         acc + f(3)
      }

      // then
      assertEquals(vals, listOf(4, 6, 0))
   }

   @Test
   fun `simple test - list f app - D`() {
      // when
      val vals = funs(3)

      // then
      assertEquals(vals, listOf(4, 6, 0))
   }

   @Test
   fun `simple test - list f app - E`() {
      // when
      val vals = funs invoke 3

      // then
      assertEquals(vals, listOf(4, 6, 0))
   }

   @Test
   fun `simple test - list f compose 1`() {
      // given
      val times4 = { x: Int -> x * 4 }

      // when
      val compositeFuns1: List<(Int) -> Int> = funs.map { f ->
         f andThen times4
      }

      val compositeFuns2: List<(Int) -> Int> = funs.map { f ->
         times4 compose f
      }

      val g1 = { z: Int -> compositeFuns1.map { f -> f(z) } }

      val g2 = { z: Int -> compositeFuns2.map { f -> f(z) } }

      // then
      assertEquals(g1(3), listOf(16, 24, 0))
      assertEquals(g2(3), g1(3))
   }

   @Test
   fun `simple test - list f compose 2`() {
      // given
      val times4 = { x: Int -> x * 4 }

      // when
      val compositeFuns1: List<(Int) -> Int> = funs.map { f ->
         times4 * f
      }

      // then
      assertEquals(compositeFuns1(3), listOf(16, 24, 0))
   }

   @Test
   fun `simple test - list f compose 3`() {
      // given
      val times4 = { x: Int -> x * 4 }

      // when
      val vals = times4 * funs invoke 4

      // then
      assertEquals(vals, listOf(20, 32, 4))
   }

   @Test
   fun `simple test - list f compose 4`() {
      // when
      val vals = funs invoke 4 pipe lift { x: Int -> x * 4 }

      // then
      assertEquals(vals, listOf(20, 32, 4))
   }

   @Test
   fun `simple test - list f compose 5`() {
      // when
      val vals = funs invoke 4 pipe +{ x: Int -> x * 4 }

      // then
      assertEquals(vals, listOf(20, 32, 4))
   }
}
