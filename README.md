# EraLock

Lock or unlock Nether and End dimensions on your Paper Minecraft server.

> **Features**
>
> **Unlike most "portal lock" plugins that simply cancel the ignition event**, EraLock actually validates the entire Nether portal structure — using a custom rewrite of NMS logic. This means:
> *   **No false positives**: If a portal isn't fully formed, we won't block it prematurely.
> *   **Accurate detection**: Works reliably even with custom portal shapes or when other plugins interfere.
>
> **Other key features include:**
- **Lock dimensions** — prevent players from entering the Nether or the End
- **Auto-sweep** — automatically teleports players out of a dimension when it gets locked, and keeps sweeping every few seconds for as long as it stays locked (catches anyone who slips in through race conditions or other plugins)
- **Smart teleport destination** — sweeps players to their bed/respawn location first, falling back to the overworld's spawn point
- **Eye of Ender blocking** — prevents inserting eyes into End Portal Frames when The End is locked
- **Dispenser protection** — prevents dispensers from inserting eyes into End Portal Frames
- **Bypass permissions** — grant specific players or admins the ability to enter, build portals in, and stay in a locked dimension
- **Per-dimension control** — lock Nether and The End independently
- **Configurable messages** — all messages use MiniMessage format, fully customizable
- **Command alias** — use `/eralock` or `/el` for quick access
- **No world name dependency** — works regardless of world names or Multiverse setup

## Requirements

- **Paper** 26.x or newer (or compatible Paper fork)
- **Java** 25 or newer

> **Note:** Built with Java 25 because that's what I had handy. I'll work on backporting to support versions 1.20.x through 26.x.

## Installation

1. Download the latest `EraLock-<version>.jar` from the [releases page](https://github.com/qhuyy/EraLock/releases)
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure `plugins/EraLock/config.yml` to your liking

## Commands

| Command                       | Alias                    | Description          | Permission      |
|--------------------------------|--------------------------|-----------------------|-----------------|
| `/eralock lock <dimension>`   | `/el lock <dimension>`   | Lock a dimension      | `eralock.admin` |
| `/eralock unlock <dimension>` | `/el unlock <dimension>` | Unlock a dimension    | `eralock.admin` |
| `/eralock reload`             | `/el reload`             | Reload configuration  | `eralock.admin` |

### Arguments

- `the_nether` — the Nether dimension
- `the_end` — the End dimension

## Permissions

```
eralock.admin              — Allows using /eralock and /el commands (default: op)
eralock.bypass.the_nether  — Allows entering, creating portals, and staying in the Nether while locked (default: op)
eralock.bypass.the_end     — Allows entering, inserting eyes of ender, and staying in the End while locked (default: op)
eralock.bypass.*           — Grants all bypass permissions above (default: op)
```

## How locking works

When you lock a dimension:

1. Every online player currently in that dimension (without bypass permission) is immediately teleported out — to their bed/respawn location if set, otherwise to the overworld's spawn point.
2. A recurring sweep keeps running every ~5 seconds for as long as the dimension stays locked, catching any player who manages to enter afterward (e.g. through another plugin, a race condition, or an admin-placed portal).
3. New portal creation (Nether ignite / End eye of ender) is blocked at the source.
4. Players with the relevant `eralock.bypass.*` permission are skipped entirely — they can enter, build portals, and stay without being swept.

Unlocking a dimension stops its sweep task and re-enables normal portal creation immediately.

## Configuration

File: `plugins/EraLock/config.yml`

```yaml
# | EraLock Configuration                                                 |
# | Lock or unlock Nether and End dimensions                              |
# +-----------------------------------------------------------------------+

# Prefix that appears before every message
# Supports MiniMessage format: https://docs.advntr.dev/minimessage/format.html
prefix: "[ <gradient:#FFFFFF:#FF008A>ᴇʀᴀʟᴏᴄᴋ</gradient> ]<reset>"

# Whether to show the prefix before messages
prefix-enabled: true

# Lock state for each dimension (default: unlocked)
lock:
  nether: false
  the_end: false

# All user-facing messages (MiniMessage format)
# Use {dimension} as placeholder for the dimension name
messages:
  no-permission: "ʙạɴ ᴋʜôɴɢ ᴄó ǫᴜʏềɴ ᴛʜựᴄ ᴛʜɪ ʟệɴʜ ɴàʏ!"
  usage: "ᴄú ᴘʜáᴘ: /<cyan>ᴇʀᴀʟᴏᴄᴋ</cyan> < ʟᴏᴄᴋ | ᴜɴʟᴏᴄᴋ | ʀᴇʟᴏᴀᴅ > < ᴛʜᴇ_ɴᴇᴛʜᴇʀ | ᴛʜᴇ_ᴇɴᴅ >"
  locked: "ᴄʜɪềᴜ ᴋʜôɴɢ ɢɪᴀɴ <cyan>{dimension}</cyan> đã ʙị ᴋʜóᴀ"
  unlocked: "ᴄʜɪềᴜ ᴋʜôɴɢ ɢɪᴀɴ <cyan>{dimension}</cyan> đã đượᴄ ᴍở ᴋʜóᴀ"
  already-locked: "ᴄʜɪềᴜ ᴋʜôɴɢ ɢɪᴀɴ <cyan>{dimension}</cyan> đᴀɴɢ ʙị ᴋʜóᴀ ʀồɪ!"
  already-unlocked: "ᴄʜɪềᴜ ᴋʜôɴɢ ɢɪᴀɴ <cyan>{dimension}</cyan> đᴀɴɢ ᴋʜôɴɢ ʙị ᴋʜóᴀ!"
  unknown-dimension: "ᴄʜɪềᴜ ᴋʜôɴɢ ɢɪᴀɴ ᴋʜôɴɢ хáᴄ địɴʜ! ᴅùɴɢ <cyan>the_nether</cyan> ʜᴏặᴄ <cyan>the_end</cyan>."
  config-reloaded: "đã ᴛảɪ ʟạɪ ᴄấᴜ ʜìɴʜ!"
  dimension-closed-kicked: "ᴄʜɪềᴜ ᴋʜôɴɢ ɢɪᴀɴ ɴàʏ đã đóɴɢ, ʙạɴ đã đượᴄ đưᴀ ᴠề ᴏᴠᴇʀᴡᴏʀʟᴅ."
```

All messages support [MiniMessage format](https://docs.advntr.dev/minimessage/format.html). Use `{dimension}` as a
placeholder for the dimension name.

## Build

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`.

## License

MIT © [hqng05]

This project is open source and available under the [MIT License](LICENSE). Feel free to use, modify, and distribute it — contributions are always welcome!

## Credits

[qhuyy](https://qhuyy.tech) — 100% caffeine-powered.
