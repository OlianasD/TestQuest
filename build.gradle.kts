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
  plugins.set(listOf("org.jetbrains.kotlin"))  // Specifica i plugin necessari
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



//NEW
dependencies {
  //implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation(kotlin("test"))
  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
  implementation("org.ow2.asm:asm:9.2")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  implementation("org.seleniumhq.selenium:selenium-java:4.21.0")
  implementation("com.github.javaparser:javaparser-core:3.23.1")
  //implementation("org.simpleframework:simple-xml:2.7.1")
  //implementation("org.w3c:dom:1.0.1")
  //implementation("org.openjfx:javafx-controls:22.0.1")
  //implementation("org.openjfx:javafx-fxml:22.0.1")
}

tasks.test {
  useJUnitPlatform()
}

/*kotlin {
  jvmToolchain(8)
}*/
