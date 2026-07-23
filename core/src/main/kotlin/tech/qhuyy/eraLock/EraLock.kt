package tech.qhuyy.eraLock

import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import tech.qhuyy.eraLock.api.EraLockAPI
import tech.qhuyy.eraLock.api.WorldType
import tech.qhuyy.eraLock.command.EraLockCommand
import tech.qhuyy.eraLock.config.LockConfig
import tech.qhuyy.eraLock.config.Messages
import tech.qhuyy.eraLock.listener.end.EndEnterListener
import tech.qhuyy.eraLock.listener.end.EndPortalBlockListener
import tech.qhuyy.eraLock.listener.nether.NetherEnterListener
import tech.qhuyy.eraLock.listener.nether.NetherPortalBlockListener

class EraLock : JavaPlugin(), EraLockAPI {
    lateinit var lockConfig: LockConfig
    lateinit var messages: Messages
    lateinit var dimensionSweeper: DimensionSweeper

    override fun onEnable() {
        lockConfig = LockConfig(this)
        messages = Messages(this)
        dimensionSweeper = DimensionSweeper(this)
        lockConfig.init()
        messages.reload()

        server.servicesManager.register(EraLockAPI::class.java, this, this, ServicePriority.Lowest)

        registerEvents()
        registerCommand()
        resumeSweeping()
    }

    override fun isLocked(worldType: WorldType): Boolean = lockConfig.isLocked(worldType)

    override fun lock(worldType: WorldType, announce: Boolean?) {
        if (lockConfig.isLocked(worldType)) return
        lockConfig.setLocked(worldType, true)
        dimensionSweeper.startSweeping(worldType)
        announceIfNeeded(worldType, locked = true, announce)
    }

    override fun unlock(worldType: WorldType, announce: Boolean?) {
        if (!lockConfig.isLocked(worldType)) return
        lockConfig.setLocked(worldType, false)
        dimensionSweeper.stopSweeping(worldType)
        announceIfNeeded(worldType, locked = false, announce)
    }

    private fun announceIfNeeded(worldType: WorldType, locked: Boolean, override: Boolean?) {
        val shouldAnnounce = override ?: messages.isAnnounceEnabled()
        if (!shouldAnnounce) return
        val name = worldType.displayName
        if (locked) {
            Bukkit.getOnlinePlayers().forEach { p ->
                p.sendActionBar(messages.actionbarLocked(name))
            }
            Bukkit.broadcast(messages.broadcastLocked(name))
        } else {
            Bukkit.getOnlinePlayers().forEach { p ->
                p.sendActionBar(messages.actionbarUnlocked(name))
            }
            Bukkit.broadcast(messages.broadcastUnlocked(name))
        }
    }

    internal fun reconcileSweeping() {
        listOf(WorldType.THE_NETHER, WorldType.THE_END).forEach { wt ->
            if (lockConfig.isLocked(wt)) {
                dimensionSweeper.startSweeping(wt)
            } else {
                dimensionSweeper.stopSweeping(wt)
            }
        }
    }

    private fun resumeSweeping() {
        reconcileSweeping()
    }

    private fun registerEvents() {
        listOf(
            EndEnterListener(lockConfig),
            EndPortalBlockListener(lockConfig),
            NetherEnterListener(lockConfig),
            NetherPortalBlockListener(lockConfig)
        ).forEach { server.pluginManager.registerEvents(it, this) }
    }

    private fun registerCommand() {
        val executor = EraLockCommand(this, lockConfig, messages)
        this.getCommand("eralock")?.apply {
            setExecutor(executor)
            tabCompleter = executor
        }
    }
}
