package tech.qhuyy.eraLock.config

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import tech.qhuyy.eraLock.EraLock

class Messages(private val plugin: EraLock) {
    private val mm = MiniMessage.miniMessage()

    private var prefix: Component = Component.empty()
    private var prefixEnabled: Boolean = true

    fun reload() {
        val cfg = plugin.config
        prefix = mm.deserialize(cfg.getString("prefix", "<gold>[EraLock]</gold>") ?: "<gold>[EraLock]</gold>")
        prefixEnabled = cfg.getBoolean("prefix-enabled", true)
    }

    fun isAnnounceEnabled(): Boolean = plugin.config.getBoolean("toggle-announce", false)

    private fun resolve(path: String, vararg pairs: Pair<String, String>): Component {
        val raw = plugin.config.getString("messages.$path")
        if (raw.isNullOrBlank()) {
            return mm.deserialize("<red>Missing config: messages.$path</red>")
        }
        var resolved: String = raw
        pairs.forEach { (k, v) -> resolved = resolved.replace("{$k}", v) }
        val msg = mm.deserialize(resolved)
        return if (prefixEnabled) prefix.append(Component.space()).append(msg) else msg
    }

    fun resolveRaw(path: String, vararg pairs: Pair<String, String>): Component {
        val raw = plugin.config.getString("messages.$path")
        if (raw.isNullOrBlank()) {
            return mm.deserialize("<red>Missing config: messages.$path</red>")
        }
        var resolved: String = raw
        pairs.forEach { (k, v) -> resolved = resolved.replace("{$k}", v) }
        return mm.deserialize(resolved)
    }

    fun noPermission() = resolve("no-permission")
    fun usage() = resolve("usage")
    fun locked(dim: String) = resolve("locked", "dimension" to dim)
    fun unlocked(dim: String) = resolve("unlocked", "dimension" to dim)
    fun alreadyLocked(dim: String) = resolve("already-locked", "dimension" to dim)
    fun alreadyUnlocked(dim: String) = resolve("already-unlocked", "dimension" to dim)
    fun unknownDimension() = resolve("unknown-dimension")
    fun configReloaded() = resolve("config-reloaded")
    fun dimensionClosedKicked() = resolve("dimension-closed-kicked")

    fun broadcastLocked(dim: String) = resolveRaw("broadcast-locked", "dimension" to dim)
    fun broadcastUnlocked(dim: String) = resolveRaw("broadcast-unlocked", "dimension" to dim)
    fun actionbarLocked(dim: String) = resolveRaw("actionbar-locked", "dimension" to dim)
    fun actionbarUnlocked(dim: String) = resolveRaw("actionbar-unlocked", "dimension" to dim)
}
