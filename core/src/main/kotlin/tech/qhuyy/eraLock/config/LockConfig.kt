package tech.qhuyy.eraLock.config

import tech.qhuyy.eraLock.EraLock
import tech.qhuyy.eraLock.api.WorldType

class LockConfig(private val plugin: EraLock) {

    fun init() {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
    }

    fun reload() {
        plugin.reloadConfig()
    }

    fun isLocked(worldType: WorldType): Boolean =
        plugin.config.getBoolean("lock.${worldType.configKey}", true)

    fun setLocked(worldType: WorldType, locked: Boolean) {
        plugin.config.set("lock.${worldType.configKey}", locked)
        plugin.saveConfig()
    }

    fun isNetherLocked(): Boolean = isLocked(WorldType.THE_NETHER)

    fun isEndLocked(): Boolean = isLocked(WorldType.THE_END)

    fun setNetherLocked(locked: Boolean) = setLocked(WorldType.THE_NETHER, locked)

    fun setEndLocked(locked: Boolean) = setLocked(WorldType.THE_END, locked)
}
