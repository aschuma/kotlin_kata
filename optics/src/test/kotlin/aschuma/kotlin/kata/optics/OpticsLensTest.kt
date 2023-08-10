package aschuma.kotlin.kata.optics

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OpticsLensTest {

   @Test
   fun `basic test modify `() {
      // given
      val person = Person("MyName", City("MyCity", Street("MyStreet")))

      // when
      val updatedStreetPerson = Person.city.street.name.modify(person) { oldStreetName -> "New$oldStreetName" }

      // then
      val manuallyUpdateStreetPerson =
         person.copy(city = person.city.copy(street = person.city.street.copy(name = "NewMyStreet")))
      assertEquals(manuallyUpdateStreetPerson, updatedStreetPerson)
   }

   @Test
   fun `basic test set`() {
      // given
      val person = Person("MyName", City("MyCity", Street("MyStreet")))

      // when
      val updatedStreetPerson = Person.city.street.name.set(person, "MyNewStreet")

      // then
      val manuallyUpdateStreetPerson =
         person.copy(city = person.city.copy(street = person.city.street.copy(name = "MyNewStreet")))
      assertEquals(manuallyUpdateStreetPerson, updatedStreetPerson)
   }
}
