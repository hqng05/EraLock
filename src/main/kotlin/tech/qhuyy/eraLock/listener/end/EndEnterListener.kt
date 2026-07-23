package tech.qhuyy.eraLock.listener.end

import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import tech.qhuyy.eraLock.config.LockConfig

class EndEnterListener(
    private val lockConfig: LockConfig
) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPortal(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_PORTAL) return
        val toWorld = event.to.world ?: return
        if (toWorld.environment != World.Environment.THE_END) return
        if (lockConfig.isEndLocked()) event.isCancelled = true
    }
}