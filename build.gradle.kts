plugins {
    kotlin("jvm") version "1.8.10"
    id("org.openjfx.javafxplugin") version "0.0.14"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-controls:17.0.2")
    implementation("org.postgresql:postgresql:42.5.0")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("MainKt")
}

/*java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}*/

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}