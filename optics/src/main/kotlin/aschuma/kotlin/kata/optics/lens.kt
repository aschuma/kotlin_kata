package aschuma.kotlin.kata.optics

// import arrow.optics.Lens
//
// val cityLens: Lens<Person, City> = Lens(
//  get = { it.city },
//  set = { person: Person, city: City -> person.copy(city = city) }
// )
//
// //City -> Street
// val streetLens: Lens<City, Street> = Lens(
//   get = { it.street },
//   set = { city: City, street: Street -> city.copy(street = street) }
// )
// //Street -> name
// val streetNameLens: Lens<Street, String> = Lens(
//   get = { it.name },
//   set = { street: Street, name: String -> street.copy(name = name) }
// )
//
// val personStreetNameLens = cityLens
//   .compose(streetLens)
//   .compose(streetNameLens)
