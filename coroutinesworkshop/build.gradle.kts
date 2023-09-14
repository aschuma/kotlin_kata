plugins {
   kotlin("jvm")
   id("org.jmailen.kotlinter") version "3.11.1"

   idea
   application
}

group = "aschuma.kotlin.kata.coroutinesworkshop"
version = "1.0-SNAPSHOT"

repositories {
   mavenCentral()
}


dependencies {
   implementation(platform("io.arrow-kt:arrow-stack:1.2.0"))
   implementation("io.arrow-kt:arrow-core")

   val kotlinxCoroutinesVersion = "1.7.3"
   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinxCoroutinesVersion")
   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinxCoroutinesVersion")

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
   mainClass.set("aschuma.kotlin.kata.coroutines.MainKt")
}


