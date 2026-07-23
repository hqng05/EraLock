package tech.qhuyy.eraLock.api

enum class WorldType(
    val configKey: String,
    val displayName: String,
    val bypassPermission: String
) {
    THE_NETHER("nether", "Nether", "eralock.bypass.the_nether"),
    THE_END("the_end", "The End", "eralock.bypass.the_end");

    companion object {
        private val map = entries.associateBy { it.name.lowercase() }

        fun fromKey(key: String): WorldType? = map[key]

        fun fromConfigKey(key: String): WorldType? =
            entries.firstOrNull { it.configKey == key }
    }
}
