plugins {
    kotlin("jvm") version "1.8.20"
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"
    id("org.jmailen.kotlinter") version "3.11.1"

    idea
    application
}


group = "aschuma.kotlin.kata.optics"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val arrowVersion = "1.2.0-RC"
dependencies {
   implementation("io.arrow-kt:arrow-core:${arrowVersion}")
   implementation("io.arrow-kt:arrow-optics:${arrowVersion}")
   ksp("io.arrow-kt:arrow-optics-ksp-plugin:$arrowVersion")

   testImplementation(kotlin("test"))
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
