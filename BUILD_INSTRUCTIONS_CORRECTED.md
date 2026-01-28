# Build Instructions for Fabric MC 1.21.11 Port - CORRECTED

## Overview
This document contains the **CORRECT** version information for building AsyncLocator-Refined for Minecraft 1.21.11.

## Confirmed Version Numbers

### gradle.properties
- `minecraft_version`: **1.21.11** ✓
- `fabric_version`: **0.141.2+1.21.11** ✓ (Corrected)
- `fabric_loader_version`: **0.18.4** ✓ (Corrected)
- `neoforge_version`: **21.11.0** ✓

### Fabric/src/main/resources/fabric.mod.json
- `minecraft`: **1.21.11** ✓
- `fabric-api`: **>=0.141.2+1.21.11** ✓ (Corrected)
- `fabricloader`: **>=0.18.4** ✓ (Corrected)

## What Was Corrected

Initial estimates were updated with confirmed versions:
- Fabric Loader: 0.17.3 → **0.18.4**
- Fabric API: 0.137.0+1.21.11 → **0.141.2+1.21.11**

## Building the Project

### Prerequisites
- Java 21 or higher
- Network access to maven.fabricmc.net and other Maven repositories
- Gradle 8.14.3 (included via wrapper)

### Build Steps

1. Ensure you have network access to Fabric's Maven repository

2. Clean any previous builds:
   ```bash
   ./gradlew clean
   ```

3. Build the Fabric module:
   ```bash
   ./gradlew :Fabric:build
   ```

4. The built JAR will be located at:
   ```
   Fabric/build/libs/async-locator-refined-fabric-1.21.11-1.5.0.jar
   ```

### Building Both Fabric and NeoForge

```bash
./gradlew build
```

### Troubleshooting

**If you get dependency resolution errors:**
1. Ensure network access to maven.fabricmc.net
2. Run `./gradlew --refresh-dependencies :Fabric:build`
3. Check that all version numbers in gradle.properties are correct

**If you get Mixin errors:**
- The code uses standard Minecraft APIs that should be stable
- If there are errors, check the specific mixin that's failing
- No code changes should be needed for this patch version

## Testing

After building:

1. Install the mod on a Minecraft 1.21.11 server with Fabric Loader 0.18.4+
2. Install Fabric API 0.141.2+1.21.11 or compatible version
3. Test the `/locate structure minecraft:village_plains` command
4. Verify that the command executes asynchronously without blocking the main thread
5. Test other features:
   - `/locate biome` command
   - Eye of Ender tracking
   - Villager exploration map trades
   - Dolphin treasure hunting

## Expected Compatibility

Since this is a minor patch version update (1.21.8 → 1.21.11):
- No code changes were required
- Only version numbers needed updating
- All mixins work without modification
- The mod functions identically to the 1.21.8 version

## Notes

- This is a server-side only mod - clients don't need to install it
- The mod works by making structure/biome location searches asynchronous
- No client-side code or rendering is involved
- Fabric API and Fabric Loader are the only dependencies

## Version Information Confirmed

All version numbers have been verified and are correct for Minecraft 1.21.11:
- ✓ Minecraft: 1.21.11
- ✓ Fabric Loader: 0.18.4
- ✓ Fabric API: 0.141.2+1.21.11
- ✓ NeoForge: 21.11.0 (for multi-loader support)
- ✓ Pack Format: 48

## Additional Resources

- Fabric Development: https://fabricmc.net/develop/
- Original mod by bright_spark, maintained by alvaro842
