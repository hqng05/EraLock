plugins {
    kotlin("jvm") version libs.versions.kotlin.get() apply false
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
}
