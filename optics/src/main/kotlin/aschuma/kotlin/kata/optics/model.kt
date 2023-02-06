package aschuma.kotlin.kata.optics

import arrow.optics.optics

@optics
data class Person(val name: String, val city: City) {
   companion object // do not remove !
}

@optics
data class City(val name: String, val street: Street) {
   companion object
}

@optics
data class Street(val name: String) {
   companion object
}
