plugins {
   id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"

   kotlin("jvm") version "1.8.22" apply false
}

rootProject.name = "kotlin-kata"
include("optics", "dsl", "coroutines")

