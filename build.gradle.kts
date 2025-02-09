import java.util.Calendar

plugins {
  java
  id("org.springframework.boot") version "3.4.1"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.openjfx.javafxplugin") version("0.1.0")
  id("com.github.hierynomus.license") version("0.16.1")
  id("org.ec4j.editorconfig") version("0.1.0")
}

group = "io.ashisht.keystore_wiz"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.openjfx:javafx-controls:21.0.5") // Use the version you need (e.g., 20)
  implementation("org.openjfx:javafx-fxml:21.0.5")
  implementation("org.openjfx:javafx-graphics:21.0.5")
  implementation("org.openjfx:javafx-base:21.0.5")
  compileOnly("org.projectlombok:lombok")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  annotationProcessor("org.projectlombok:lombok")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.check {
  dependsOn("editorconfigCheck")
  dependsOn("licenseTest")
}

tasks.named("licenseTest") {
  enabled = false
}

javafx {
  version = "21.0.5"  // JavaFX version
  modules = listOf("javafx.controls", "javafx.fxml")  // Required modules
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
  jvmArgs = listOf(
    "--module-path", classpath.asPath,
    "--add-modules", "javafx.controls,javafx.fxml"
  )
}

extra["year"] = Calendar.getInstance().get(Calendar.YEAR).toString()
extra["name"] = "Ashish Thakur"
extra["email"] = "ashish.thakur1110@gmail.com"

license {
  header = file("HEADER")
  strictCheck = true
  excludePatterns = setOf("**/*.json", "**/*.xml", "**/*.properties", "**/*.fxml", "**/*.css", "**/*.js", "**/*.yaml", "**/*.yml", "src/test/**/*")
  ext {
    ext["year"] = extra["year"]
    ext["name"] = extra["name"]
    ext["email"] = extra["email"]
  }
}
