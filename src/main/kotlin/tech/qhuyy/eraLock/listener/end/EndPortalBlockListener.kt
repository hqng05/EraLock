package tech.qhuyy.eraLock.listener.end

import org.bukkit.Material
import org.bukkit.block.data.type.Dispenser
import org.bukkit.block.data.type.EndPortalFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.event.player.PlayerInteractEvent
import tech.qhuyy.eraLock.config.LockConfig

class EndPortalBlockListener(
    private val lockConfig: LockConfig
) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerInteract(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        val block = e.clickedBlock ?: return
        if (block.type != Material.END_PORTAL_FRAME) return
        val item = e.item ?: return
        if (item.type != Material.ENDER_EYE) return
        if (!lockConfig.isEndLocked()) return
        val frameData = block.blockData as? EndPortalFrame ?: return
        if (frameData.hasEye()) return
        e.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDispense(e: BlockDispenseEvent) {
        if (e.item.type != Material.ENDER_EYE) return
        if (!lockConfig.isEndLocked()) return
        val dispenserData = e.block.blockData
        val facing = (dispenserData as? Dispenser)?.facing ?: return
        val targetBlock = e.block.getRelative(facing)
        if (targetBlock.type != Material.END_PORTAL_FRAME) return
        val frameData = targetBlock.blockData as? EndPortalFrame ?: return
        if (frameData.hasEye()) return
        e.isCancelled = true
    }
}