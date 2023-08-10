plugins {
   kotlin("jvm")
   kotlin("plugin.serialization") version "1.9.0"
   id("org.jmailen.kotlinter") version "3.11.1"

   idea
   application
}

group = "aschuma.kotlin.kata.serialization"
version = "1.0-SNAPSHOT"

repositories {
   mavenCentral()
}


dependencies {
   implementation("io.arrow-kt:arrow-core:1.2.0")
   implementation("io.arrow-kt:arrow-core-serialization:1.2.0")
   implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

   testImplementation(kotlin("test"))
   testImplementation("io.kotest:kotest-assertions-core:5.6.1")
}

tasks.named<Test>("test") {
   useJUnitPlatform()
}

kotlin {
   jvmToolchain(17)
}

application {
   mainClass.set("aschuma.kotlin.kata.serialization.MainKt")
}


