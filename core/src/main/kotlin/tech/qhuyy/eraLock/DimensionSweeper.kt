package tech.qhuyy.eraLock

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import tech.qhuyy.eraLock.api.WorldType

class DimensionSweeper(private val plugin: EraLock) {

    private val activeTasks = mutableMapOf<WorldType, BukkitTask>()

    fun startSweeping(worldType: WorldType) {
        stopSweeping(worldType)
        val environment = environmentOf(worldType) ?: return
        sweep(environment, worldType.bypassPermission)
        val task = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            sweep(environment, worldType.bypassPermission)
        }, 100L, 100L)
        activeTasks[worldType] = task
    }

    fun stopSweeping(worldType: WorldType) {
        activeTasks.remove(worldType)?.cancel()
    }

    private fun environmentOf(worldType: WorldType): World.Environment? = when (worldType) {
        WorldType.THE_NETHER -> World.Environment.NETHER
        WorldType.THE_END -> World.Environment.THE_END
    }

    private fun sweep(environment: World.Environment, bypassPermission: String) {
        val fallback = findOverworldFallback() ?: run {
            plugin.logger.warning("No overworld found to send players back to for $environment sweep.")
            return
        }

        Bukkit.getOnlinePlayers()
            .filter { it.world.environment == environment }
            .filter { !it.hasPermission(bypassPermission) }
            .forEach { player -> teleportOut(player, fallback) }
    }

    private fun teleportOut(player: Player, fallback: World) {
        val destination = resolveDestination(player, fallback)
        val success = player.teleport(destination)
        if (success) {
            player.sendMessage(plugin.messages.dimensionClosedKicked())
        } else {
            plugin.logger.warning("Failed to teleport ${player.name} from ${player.world.name} to ${destination.world?.name ?: "unknown"}")
        }
    }

    private fun resolveDestination(player: Player, fallback: World): Location {
        return player.getRespawnLocation(false)
            ?.takeIf { it.world?.environment == World.Environment.NORMAL }
            ?: fallback.spawnLocation
    }

    private fun findOverworldFallback(): World? {
        Bukkit.getWorld("world")?.let { if (it.environment == World.Environment.NORMAL) return it }
        return Bukkit.getWorlds().firstOrNull { it.environment == World.Environment.NORMAL }
    }
}
