@file:UseSerializers(
   arrow.core.serialization.EitherSerializer::class,
   arrow.core.serialization.IorSerializer::class,
   arrow.core.serialization.NonEmptyListSerializer::class,
   arrow.core.serialization.NonEmptySetSerializer::class,
   arrow.core.serialization.OptionSerializer::class,
   arrow.core.serialization.ValidatedSerializer::class
)

package aschuma.kotlin.kata.serialization.model

import arrow.core.Either
import arrow.core.Option
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@JvmInline
@Serializable
value class FirstName(val value: String)

@JvmInline
@Serializable
value class LastName(val value: String)

@JvmInline
@Serializable
value class Age(val value: Int?)

@Serializable
data class Person(val firstName: FirstName, val lastName: LastName, val age: Age?)

@Serializable
data class PersonM(val firstNameE: Either<String, FirstName>, val lastNameOpt: Option<LastName>, val age: Age?)
