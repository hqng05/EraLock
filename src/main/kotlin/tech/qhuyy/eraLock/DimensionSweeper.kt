package tech.qhuyy.eraLock

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class DimensionSweeper(private val plugin: EraLock) {

    private val activeTasks = mutableMapOf<World.Environment, BukkitTask>()

    fun startSweeping(environment: World.Environment, bypassPermission: String) {
        stopSweeping(environment)
        sweep(environment, bypassPermission)
        val task = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            sweep(environment, bypassPermission)
        }, 100L, 100L)
        activeTasks[environment] = task
    }

    fun stopSweeping(environment: World.Environment) {
        activeTasks.remove(environment)?.cancel()
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
        player.teleport(destination)
        player.sendMessage(plugin.messages.dimensionClosedKicked())
    }

    private fun resolveDestination(player: Player, fallback: World): Location {
        return player.respawnLocation
            ?.takeIf { it.world?.environment == World.Environment.NORMAL }
            ?: fallback.spawnLocation
    }

    private fun findOverworldFallback(): World? {
        Bukkit.getWorld("world")?.let { if (it.environment == World.Environment.NORMAL) return it }
        return Bukkit.getWorlds().firstOrNull { it.environment == World.Environment.NORMAL }
    }
}