plugins {
  kotlin("jvm") version "1.8.20"
  id("org.jmailen.kotlinter") version "3.11.1"

  idea
  application
}


group = "aschuma.kotlin.kata.coroutines"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val arrowVersion = "1.2.0-RC"
dependencies {
  implementation(platform("io.arrow-kt:arrow-stack:${arrowVersion}"))
  implementation("io.arrow-kt:arrow-core:${arrowVersion}")
  testImplementation(kotlin("test"))
  testImplementation("io.kotest:kotest-assertions-core:5.6.1")
}

kotlin {
  sourceSets.main {
    kotlin.srcDir("build/generated/ksp/main/kotlin")
  }
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(11)
}

application {
  mainClass.set("aschuma.kotlin.kata.optics.MainKt")
}
