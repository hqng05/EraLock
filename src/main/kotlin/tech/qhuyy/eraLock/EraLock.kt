package tech.qhuyy.eraLock

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import tech.qhuyy.eraLock.command.EraLockCommand
import tech.qhuyy.eraLock.config.LockConfig
import tech.qhuyy.eraLock.config.Messages
import tech.qhuyy.eraLock.listener.end.EndEnterListener
import tech.qhuyy.eraLock.listener.end.EndPortalBlockListener
import tech.qhuyy.eraLock.listener.nether.NetherEnterListener
import tech.qhuyy.eraLock.listener.nether.NetherPortalBlockListener

class EraLock : JavaPlugin() {
    lateinit var lockConfig: LockConfig
    lateinit var messages: Messages

    override fun onEnable() {
        lockConfig = LockConfig(this)
        messages = Messages(this)
        lockConfig.init()
        messages.reload()
        registerEvents()
        registerCommand()
        Bukkit.getConsoleSender().sendMessage(messages.pluginEnabled())
    }

    override fun onDisable() {
        if (::messages.isInitialized) {
            Bukkit.getConsoleSender().sendMessage(messages.pluginDisabled())
        }
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
