plugins {
    kotlin("jvm")
    java
}

kotlin {
    jvmToolchain(21)
}

// EraLock API — pure Kotlin module, no Bukkit dependency.
// Other plugins can depend on this to interact with EraLock.
