plugins {
   id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"

   kotlin("jvm") version "1.9.0" apply false
}

rootProject.name = "kotlin-kata"
include("optics", "dsl", "coroutines", "coroutinesworkshop", "serialization")

