package tech.qhuyy.eraLock.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import tech.qhuyy.eraLock.EraLock
import tech.qhuyy.eraLock.api.WorldType
import tech.qhuyy.eraLock.config.LockConfig
import tech.qhuyy.eraLock.config.Messages

class EraLockCommand(
    private val plugin: EraLock,
    private val lockConfig: LockConfig,
    private val messages: Messages
) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("eralock.admin")) {
            sender.sendMessage(messages.noPermission())
            return true
        }
        if (args.isEmpty()) {
            sender.sendMessage(messages.usage())
            return true
        }
        when (args[0].lowercase()) {
            "lock" -> handleLock(sender, args)
            "unlock" -> handleUnlock(sender, args)
            "reload" -> handleReload(sender)
            else -> sender.sendMessage(messages.usage())
        }
        return true
    }

    private fun parseWorldType(raw: String): WorldType? = when (raw.lowercase()) {
        "the_nether" -> WorldType.THE_NETHER
        "the_end" -> WorldType.THE_END
        else -> null
    }

    private fun parseAnnounceOverride(args: Array<String>): Boolean? {
        if (args.size < 3) return null
        val raw = args[2].lowercase().removePrefix("announce:")
        return when (raw) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }

    private fun handleLock(sender: CommandSender, args: Array<String>) {
        if (args.size < 2) {
            sender.sendMessage(messages.usage())
            return
        }
        val worldType = parseWorldType(args[1]) ?: run {
            sender.sendMessage(messages.unknownDimension())
            return
        }
        if (lockConfig.isLocked(worldType)) {
            sender.sendMessage(messages.alreadyLocked(worldType.displayName))
            return
        }
        val announce = parseAnnounceOverride(args)
        plugin.lock(worldType, announce)
        sender.sendMessage(messages.locked(worldType.displayName))
    }

    private fun handleUnlock(sender: CommandSender, args: Array<String>) {
        if (args.size < 2) {
            sender.sendMessage(messages.usage())
            return
        }
        val worldType = parseWorldType(args[1]) ?: run {
            sender.sendMessage(messages.unknownDimension())
            return
        }
        if (!lockConfig.isLocked(worldType)) {
            sender.sendMessage(messages.alreadyUnlocked(worldType.displayName))
            return
        }
        val announce = parseAnnounceOverride(args)
        plugin.unlock(worldType, announce)
        sender.sendMessage(messages.unlocked(worldType.displayName))
    }

    private fun handleReload(sender: CommandSender) {
        plugin.reloadConfig()
        lockConfig.reload()
        messages.reload()
        plugin.reconcileSweeping()
        sender.sendMessage(messages.configReloaded())
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String> {
        if (!sender.hasPermission("eralock.admin")) return emptyList()
        if (args.size == 1) {
            return listOf("lock", "unlock", "reload").filter { it.startsWith(args[0].lowercase()) }
        }
        if (args.size == 2) {
            return when (args[0].lowercase()) {
                "lock" -> buildList {
                    if (!lockConfig.isLocked(WorldType.THE_NETHER)) add("the_nether")
                    if (!lockConfig.isLocked(WorldType.THE_END)) add("the_end")
                }.filter { it.startsWith(args[1], ignoreCase = true) }

                "unlock" -> buildList {
                    if (lockConfig.isLocked(WorldType.THE_NETHER)) add("the_nether")
                    if (lockConfig.isLocked(WorldType.THE_END)) add("the_end")
                }.filter { it.startsWith(args[1], ignoreCase = true) }

                else -> emptyList()
            }
        }
        if (args.size == 3 && args[0].lowercase() in listOf("lock", "unlock")) {
            return listOf("announce:true", "announce:false")
                .filter { it.startsWith(args[2].lowercase()) }
        }
        return emptyList()
    }
}
