package tech.qhuyy.eraLock.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import tech.qhuyy.eraLock.EraLock
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

    private fun handleLock(sender: CommandSender, args: Array<String>) {
        if (args.size < 2) {
            sender.sendMessage(messages.usage())
            return
        }
        when (args[1].lowercase()) {
            "the_nether" -> {
                if (lockConfig.isNetherLocked()) {
                    sender.sendMessage(messages.alreadyLocked("Nether"))
                    return
                }
                lockConfig.setNetherLocked(true)
                sender.sendMessage(messages.locked("Nether"))
            }

            "the_end" -> {
                if (lockConfig.isEndLocked()) {
                    sender.sendMessage(messages.alreadyLocked("The End"))
                    return
                }
                lockConfig.setEndLocked(true)
                sender.sendMessage(messages.locked("The End"))
            }

            else -> sender.sendMessage(messages.unknownDimension())
        }
    }

    private fun handleUnlock(sender: CommandSender, args: Array<String>) {
        if (args.size < 2) {
            sender.sendMessage(messages.usage())
            return
        }
        when (args[1].lowercase()) {
            "the_nether" -> {
                if (!lockConfig.isNetherLocked()) {
                    sender.sendMessage(messages.alreadyUnlocked("Nether"))
                    return
                }
                lockConfig.setNetherLocked(false)
                sender.sendMessage(messages.unlocked("Nether"))
            }

            "the_end" -> {
                if (!lockConfig.isEndLocked()) {
                    sender.sendMessage(messages.alreadyUnlocked("The End"))
                    return
                }
                lockConfig.setEndLocked(false)
                sender.sendMessage(messages.unlocked("The End"))
            }

            else -> sender.sendMessage(messages.unknownDimension())
        }
    }

    private fun handleReload(sender: CommandSender) {
        plugin.reloadConfig()
        messages.reload()
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
                    if (!lockConfig.isNetherLocked()) add("the_nether")
                    if (!lockConfig.isEndLocked()) add("the_end")
                }.filter { it.startsWith(args[1], ignoreCase = true) }

                "unlock" -> buildList {
                    if (lockConfig.isNetherLocked()) add("the_nether")
                    if (lockConfig.isEndLocked()) add("the_end")
                }.filter { it.startsWith(args[1], ignoreCase = true) }

                else -> emptyList()
            }
        }
        return emptyList()
    }
}
