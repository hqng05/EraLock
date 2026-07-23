# EraLock

Lock or unlock Nether and End dimensions on your Paper Minecraft server.

## Features

- **Lock dimensions** — prevent players from entering the Nether or the End
- **Portal blocking** — prevents portal creation (ignite) when Nether is locked
- **Eye of Ender blocking** — prevents inserting eyes into End Portal Frames when The End is locked
- **Dispenser protection** — prevents dispensers from inserting eyes into End Portal Frames
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
|-------------------------------|--------------------------|----------------------|-----------------|
| `/eralock lock <dimension>`   | `/el lock <dimension>`   | Lock a dimension     | `eralock.admin` |
| `/eralock unlock <dimension>` | `/el unlock <dimension>` | Unlock a dimension   | `eralock.admin` |
| `/eralock reload`             | `/el reload`             | Reload configuration | `eralock.admin` |

### Arguments

- `the_nether` — the Nether dimension
- `the_end` — the End dimension

## Permissions

```
eralock.admin — Allows using /eralock and /el commands (default: op)
```

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
