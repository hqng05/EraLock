package tech.qhuyy.eraLock.listener.nether

import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import tech.qhuyy.eraLock.config.LockConfig

class NetherEnterListener(
    private val lockConfig: LockConfig
) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPortal(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return
        val toWorld = event.to?.world ?: return
        if (toWorld.environment != World.Environment.NETHER) return
        if (lockConfig.isNetherLocked()) event.isCancelled = true
    }
}