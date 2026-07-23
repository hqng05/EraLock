package tech.qhuyy.eraLock.model

import kotlinx.serialization.Serializable
import org.bukkit.World

@Serializable
data class WorldStatus(
    val worldName: String,
    val isLocked: Boolean
) {
    companion object {
        fun parseToList(list: List<World>, def: Boolean? = false): List<WorldStatus> {
            val wl = mutableListOf<WorldStatus>()
            list.forEach { wl.add(WorldStatus(it.name, def!!)) }
            return wl
        }
    }
}