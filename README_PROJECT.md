# AsyncLocator-Refined - Fabric MC 1.21.11

This repository has been successfully ported to **Minecraft 1.21.11** for **Fabric only**.

## Quick Start

### Build the Mod

```bash
./gradlew clean
./gradlew :Fabric:build
```

**Output:** `Fabric/build/libs/async-locator-refined-fabric-1.21.11-1.5.0.jar`

### Requirements

**Build:**
- Java 21+
- Internet connection (for dependencies)

**Runtime:**
- Minecraft 1.21.11
- Fabric Loader 0.18.4+
- Fabric API 0.141.2+1.21.11+

## What Changed

### Port from 1.21.8 → 1.21.11

✅ **Updated Versions:**
- Minecraft: 1.21.8 → 1.21.11
- Fabric Loader: 0.17.3 → 0.18.4
- Fabric API: 0.136.1+1.21.8 → 0.141.2+1.21.11
- Pack Format: 9 → 48

✅ **Removed NeoForge:**
- Deleted entire NeoForge module
- Removed NeoForge from build configuration
- Project now Fabric-only

✅ **Source Code:**
- No code changes required (stable API between patch versions)

## Project Structure

```
AsyncLocator-Refined/
├── Common/              # Shared logic and mixins
├── Fabric/              # Fabric-specific implementation
├── gradle/
├── BUILD_INSTRUCTIONS.md
├── PORT_SUMMARY.md
├── check_fabric_version.sh
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Documentation

- **[BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)** - Detailed build guide
- **[PORT_SUMMARY.md](PORT_SUMMARY.md)** - Complete port summary
- **check_fabric_version.sh** - Version verification script

## Features

This mod makes structure and biome location searches asynchronous to prevent server lag:

- `/locate structure` - Async structure searching
- `/locate biome` - Async biome searching  
- Eye of Ender tracking - Async stronghold location
- Villager trades - Async exploration map generation
- Dolphin treasure - Async treasure location

**Server-side only** - Clients don't need to install this mod.

## Credits

- **Original Author:** bright_spark
- **Maintainer:** alvaro842
- **1.21.11 Port:** GitHub Copilot Agent

## License

MIT License - See [LICENSE](LICENSE) file

---

For build instructions and troubleshooting, see [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)
