package tech.qhuyy.eraLock

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin
import tech.qhuyy.eraLock.config.WorldStatusParser

class EraLock : JavaPlugin() {
    lateinit var worldStatusParser: WorldStatusParser
    private val affectWorld: List<World> = Bukkit.getWorlds().filter {
        it.environment == World.Environment.NETHER ||
                it.environment == World.Environment.THE_END
    }

    override fun onEnable() {
        worldStatusParser = WorldStatusParser(
            this,
            affectWorld
        )
        worldStatusParser.init()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
