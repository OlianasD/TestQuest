plugins {
  id("org.jetbrains.kotlin.jvm") version "1.9.0"
  id("org.jetbrains.intellij") version "1.15.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain(17)
}

intellij {
  version.set("2023.2.6")
  plugins.set(listOf("java", "org.jetbrains.kotlin"))
  //plugins.set(listOf("org.jetbrains.kotlin"))
  //plugins.set(listOf("java", "org.jetbrains.kotlin", "com.intellij.java", "com.intellij.ui"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}


tasks {
  patchPluginXml {
    sinceBuild.set("232.0")
    untilBuild.set("233.*")
  }
}

val ktorVersion = "2.3.2"

dependencies {
  testImplementation(kotlin("test"))
  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
  implementation("org.ow2.asm:asm:9.2")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  implementation("org.seleniumhq.selenium:selenium-java:4.21.0")
  implementation("com.github.javaparser:javaparser-core:3.23.1")
  implementation("org.slf4j:slf4j-api:2.0.9")
  implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
  implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
  //implementation("com.jetbrains.intellij.java:java-psi:RELEASE_VERSION")
}

tasks.test {
  useJUnitPlatform()
}
