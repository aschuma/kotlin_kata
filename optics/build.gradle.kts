plugins {
   kotlin("jvm") version "1.9.0"
   id("com.google.devtools.ksp") version "1.9.0-1.0.11"

   idea
   application
}


group = "aschuma.kotlin.kata.optics"
version = "1.0-SNAPSHOT"

repositories {
   mavenCentral()
}

val arrowVersion = "1.2.0"
dependencies {
   implementation(platform("io.arrow-kt:arrow-stack:$arrowVersion"))
   implementation("io.arrow-kt:arrow-core")
   implementation("io.arrow-kt:arrow-optics")
   ksp("io.arrow-kt:arrow-optics-ksp-plugin:$arrowVersion")

   testImplementation(kotlin("test"))
   testImplementation("io.kotest:kotest-assertions-core:5.6.1")
}

kotlin {
   jvmToolchain(17)
   sourceSets.main {
      kotlin.srcDirs("build/generated/ksp/main/kotlin", "src/main/kotlin")
   }
}

tasks.named<Test>("test") {
   useJUnitPlatform()
}

application {
   mainClass.set("aschuma.kotlin.kata.optics.MainKt")
}
