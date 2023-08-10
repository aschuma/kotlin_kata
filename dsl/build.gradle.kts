plugins {
   kotlin("jvm")
   id("org.jmailen.kotlinter") version "3.11.1"

   idea
   application
}

group = "aschuma.kotlin.kata.dsl"
version = "1.0-SNAPSHOT"

repositories {
   mavenCentral()
}


dependencies {
   implementation(platform("io.arrow-kt:arrow-stack:1.2.0"))
   implementation("io.arrow-kt:arrow-core")

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
   mainClass.set("aschuma.kotlin.kata.dsl.MainKt")
}
