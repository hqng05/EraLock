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

- **Paper** 1.26.2 or newer (or compatible Paper fork)
- **Java** 25 or newer

## Installation

1. Download the latest `EraLock-<version>.jar` from the [releases page](https://github.com/qhuyy/EraLock/releases)
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure `plugins/EraLock/config.yml` to your liking

## Commands

| Command | Alias | Description | Permission |
|---------|-------|-------------|------------|
| `/eralock lock <dimension>` | `/el lock <dimension>` | Lock a dimension | `eralock.admin` |
| `/eralock unlock <dimension>` | `/el unlock <dimension>` | Unlock a dimension | `eralock.admin` |
| `/eralock reload` | `/el reload` | Reload configuration | `eralock.admin` |

### Arguments

- `the_nether` — the Nether dimension
- `the_end` — the End dimension

Tab completion is state-aware: the command only suggests dimensions that can actually be locked/unlocked.

## Permissions

```
eralock.admin — Allows using /eralock and /el commands (default: op)
```

## Configuration

File: `plugins/EraLock/config.yml`

```yaml
# Prefix that appears before every message
prefix: "<gold>[EraLock]</gold>"
prefix-enabled: true

# Lock state for each dimension (default: locked)
lock:
  nether: true
  the_end: true

# All user-facing messages (MiniMessage format)
messages:
  no-permission: "<red>You don't have permission!</red>"
  usage: "<gold>/eralock <lock|unlock|reload> <the_nether|the_end></gold>"
  locked: "<green>✔ Locked <yellow>{dimension}</yellow></green>"
  unlocked: "<green>✔ Unlocked <yellow>{dimension}</yellow></green>"
  already-locked: "<yellow>{dimension} is already locked!</yellow>"
  already-unlocked: "<yellow>{dimension} is already unlocked!</yellow>"
  unknown-dimension: "<red>Unknown dimension! Use the_nether or the_end.</red>"
  config-reloaded: "<green>Configuration reloaded!</green>"
  plugin-enabled: "<gold>EraLock enabled</gold>"
  plugin-disabled: "<gold>EraLock disabled</gold>"
```

All messages support [MiniMessage format](https://docs.advntr.dev/minimessage/format.html).
Use `{dimension}` as a placeholder for the dimension name.

## Build

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`.

## License

[MIT](LICENSE)

## Credits

- [qhuyy](https://qhuyy.tech) — author
