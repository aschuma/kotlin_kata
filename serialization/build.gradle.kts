plugins {
   kotlin("jvm")
   kotlin("plugin.serialization")
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
   val arrowVersion = "1.2.4"
   implementation("io.arrow-kt:arrow-core:${arrowVersion}")
   implementation("io.arrow-kt:arrow-core-serialization:${arrowVersion}")

   implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")

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


