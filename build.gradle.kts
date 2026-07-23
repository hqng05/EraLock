plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.kotlin.stdlib)
}

kotlin {
    jvmToolchain(21)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    jar {
        archiveBaseName.set("EraLock")
        archiveClassifier.set("source")
    }

    shadowJar {
        archiveBaseName.set("EraLock")
        archiveClassifier.set("")

        relocate("kotlin", "tech.qhuyy.eraLock.libs.kotlin")

        exclude("META-INF/*.kotlin_module")
        exclude("**/*.kotlin_builtins")
        exclude("**/*.kotlin_metadata")
        exclude("META-INF/maven/**")
        exclude("META-INF/LICENSE*")
        exclude("META-INF/NOTICE*")
        exclude("META-INF/DEPENDENCIES")
        exclude("module-info.class")
        exclude("**/*.dsa")
        exclude("**/*.rsa")
        exclude("**/*.sf")
        minimize()
    }

    runServer {
        minecraftVersion(libs.versions.minecraft.get())
        jvmArgs("-Xms2G", "-Xmx2G", "-Dcom.mojang.eula.agree=true")
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}