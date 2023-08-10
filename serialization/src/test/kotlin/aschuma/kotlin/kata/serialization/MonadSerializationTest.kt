package aschuma.kotlin.kata.serialization

import arrow.core.None
import arrow.core.Option
import arrow.core.left
import arrow.core.right
import aschuma.kotlin.kata.serialization.model.Age
import aschuma.kotlin.kata.serialization.model.FirstName
import aschuma.kotlin.kata.serialization.model.LastName
import aschuma.kotlin.kata.serialization.model.PersonM
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MonadSerializationTest {

   @Test
   fun `simple test - right `() {
      // given
      val person = PersonM(FirstName("Zaha").right(), Option.fromNullable(LastName("Hadid")), Age(42))

      // when
      val personJsonString = Json.encodeToString(PersonM.serializer(), person)

      // then
      assertEquals(personJsonString, """{"firstNameE":{"right":"Zaha"},"lastNameOpt":"Hadid","age":42}""")

      // when
      val personFromJson = Json.decodeFromString(PersonM.serializer(), personJsonString)

      // then
      assertEquals(personFromJson, person)
   }

   @Test
   fun `simple test - left `() {
      // given
      val person = PersonM("error".left(), None, Age(42))

      // when
      val json = Json {
         explicitNulls = false
         // prettyPrint = true
         // encodeDefaults = true
         // ignoreUnknownKeys = true
         // coerceInputValues = true
         // isLenient = true
      }
      val personJsonString = json.encodeToString(PersonM.serializer(), person)

      // then
      assertEquals(personJsonString, """{"firstNameE":{"left":"error"},"lastNameOpt":null,"age":42}""")

      // when
      val personFromJson = json.decodeFromString(PersonM.serializer(), personJsonString)

      // then
      assertEquals(personFromJson, person)
   }
}
