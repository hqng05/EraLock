package tech.qhuyy.eraLock.config

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.World
import tech.qhuyy.eraLock.EraLock
import tech.qhuyy.eraLock.model.WorldStatus
import java.io.File

class WorldStatusParser(
    private val eraLock: EraLock,
    private val affectWorld: List<World>
) {
    private val worldDataFile = File(eraLock.dataFolder, "world.json")
    private val loadedWorlds = mutableListOf<WorldStatus>()

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun init() {
        loadedWorlds.clear()
        checkAndCreateDataFile()
        loadWorldsFromConfig()
    }

    private fun loadWorldsFromConfig() {
        if(!worldDataFile.exists() || worldDataFile.length() == 0L || worldDataFile.readText().isBlank()) {
            val wl = WorldStatus.parseToList(affectWorld)
            saveWorldsToConfig(wl)
            loadedWorlds.addAll(wl)
            return
        }

        try {
            val p = json.decodeFromString<List<WorldStatus>>(worldDataFile.readText(Charsets.UTF_8))
            loadedWorlds.addAll(p)
        } catch (ex: Exception) {
            eraLock.logger.severe("failed to load worlds from $worldDataFile: ${ex.message}")
            ex.printStackTrace()
        }
    }

    private fun saveWorldsToConfig(worlds: List<WorldStatus>) {
        try {
            if (!worldDataFile.parentFile.exists()) worldDataFile.parentFile.mkdirs()
            val jsonString = json.encodeToString(worlds)
            worldDataFile.writeText(jsonString, Charsets.UTF_8)
        } catch (e: Exception) {
            eraLock.logger.severe("failed to write worlds from: ${e.message}")
        }
    }

    private fun checkAndCreateDataFile() {
        if(!worldDataFile.exists()) {
            eraLock.dataFolder.mkdirs()
            eraLock.saveResource("world.json", false)
        }
    }
}