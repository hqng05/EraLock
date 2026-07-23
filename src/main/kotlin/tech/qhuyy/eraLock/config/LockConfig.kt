package tech.qhuyy.eraLock.config

import tech.qhuyy.eraLock.EraLock

class LockConfig(private val plugin: EraLock) {

    fun init() {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
    }

    fun reload() {
        plugin.reloadConfig()
    }

    fun isNetherLocked(): Boolean =
        plugin.config.getBoolean("lock.nether", true)

    fun isEndLocked(): Boolean =
        plugin.config.getBoolean("lock.the_end", true)

    fun setNetherLocked(locked: Boolean) {
        plugin.config.set("lock.nether", locked)
        plugin.saveConfig()
    }

    fun setEndLocked(locked: Boolean) {
        plugin.config.set("lock.the_end", locked)
        plugin.saveConfig()
    }
}
