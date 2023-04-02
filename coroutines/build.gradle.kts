plugins {
    kotlin("jvm") version "1.8.0"
    id("com.google.devtools.ksp") version "1.8.0-1.0.8"
    id("org.jmailen.kotlinter") version "3.11.1"

    idea
    application
}


group = "aschuma.kotlin.kata.optics"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val arrowVersion = "1.1.4"
dependencies {
   implementation(platform("io.arrow-kt:arrow-stack:${arrowVersion}"))
   implementation("io.arrow-kt:arrow-core")
   implementation("io.arrow-kt:arrow-optics")
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
