package aschuma.kotlin.kata.serialization

import aschuma.kotlin.kata.serialization.model.Age
import aschuma.kotlin.kata.serialization.model.FirstName
import aschuma.kotlin.kata.serialization.model.LastName
import aschuma.kotlin.kata.serialization.model.Person
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InlineSerializationTest {

   @Test
   fun `simple test - inlined wrapper not available `() {
      // given
      val person = Person(FirstName("Zaha"), LastName("Hadid"), Age(42))

      // when
      val personJsonString = Json.encodeToString(Person.serializer(), person)

      // then
      assertEquals(personJsonString, """{"firstName":"Zaha","lastName":"Hadid","age":42}""")

      // when
      val personFromJson = Json.decodeFromString(Person.serializer(), personJsonString)

      // then
      assertEquals(personFromJson, person)
   }

   @Test
   fun `simple test - null element `() {
      // given
      val person = Person(FirstName("Zaha"), LastName("Hadid"), null)

      // when
      val personJsonString = Json.encodeToString(Person.serializer(), person)

      // then
      assertEquals(personJsonString, """{"firstName":"Zaha","lastName":"Hadid","age":null}""")

      // when
      val personFromJson = Json.decodeFromString(Person.serializer(), personJsonString)

      // then
      assertEquals(personFromJson, person)
   }

   @Test
   fun `simple test - null element ignored and formatted`() {
      // given
      val person = Person(FirstName("Zaha"), LastName("Hadid"), null)

      // when
      val json = Json {
         explicitNulls = false
         prettyPrint = true
         // encodeDefaults = true
         // ignoreUnknownKeys = true
         // coerceInputValues = true
         // isLenient = true
      }
      val personJsonString = json.encodeToString(Person.serializer(), person)

      // then
      assertEquals(
         personJsonString,
         """|{
         |    "firstName": "Zaha",
         |    "lastName": "Hadid"
         |}""".trimMargin("|")
      )

      // when
      val personFromJson = json.decodeFromString(Person.serializer(), personJsonString)

      // then
      assertEquals(personFromJson, person)
   }
}
