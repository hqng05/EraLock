package tech.qhuyy.eraLock.listener.nether

import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent
import tech.qhuyy.eraLock.config.LockConfig
import tech.qhuyy.eraLock.detector.NetherPortalDetector

class NetherPortalBlockListener(
    private val lockConfig: LockConfig
) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockIgnite(event: BlockIgniteEvent) {
        val block = event.block
        val currentWorld = block.world
        if (currentWorld.environment == World.Environment.NETHER) return
        if (currentWorld.environment == World.Environment.THE_END) return
        if (!lockConfig.isNetherLocked()) return
        val igniter = event.ignitingEntity as? Player
        if (igniter != null && igniter.hasPermission("eralock.bypass.the_nether")) return
        if (!NetherPortalDetector.wouldIgniteCreatePortal(block)) return
        event.isCancelled = true
    }
}