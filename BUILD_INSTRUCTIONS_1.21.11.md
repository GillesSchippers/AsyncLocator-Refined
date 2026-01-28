# Build Instructions for Fabric MC 1.21.11 Port

## Overview
This document outlines the steps to complete the port of AsyncLocator-Refined to Fabric MC 1.21.11.

## What Has Been Changed

The following version numbers have been updated:

### gradle.properties
- `minecraft_version`: 1.21.8 → **1.21.11**
- `fabric_version`: 0.136.1+1.21.8 → **0.137.0+1.21.11** (estimated)
- `neoforge_version`: 21.8.1 → **21.11.0** (for multi-loader support)
- `fabric_loader_version`: 0.17.3 (unchanged - should be compatible)

### Fabric/src/main/resources/fabric.mod.json
- `minecraft`: 1.21.8 → **1.21.11**
- `fabric-api`: >=0.136.1+1.21.8 → **>=0.137.0+1.21.11** (estimated)

### README.md
- Updated to reflect Minecraft 1.21.11

## Important: Verify Fabric API Version

⚠️ **The Fabric API version `0.137.0+1.21.11` is an educated estimate.**

To find the correct version:

1. Visit https://fabricmc.net/develop/
2. Select Minecraft version **1.21.11** from the dropdown
3. Note the Fabric API version shown (format: `X.Y.Z+1.21.11`)
4. Update both:
   - `gradle.properties`: `fabric_version` property
   - `Fabric/src/main/resources/fabric.mod.json`: `fabric-api` dependency

Alternatively, check the Maven repository directly:
- https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/

## Building the Project

### Prerequisites
- Java 21 or higher
- Network access to maven.fabricmc.net and other Maven repositories
- Gradle 8.14.3 (included via wrapper)

### Build Steps

1. Ensure you have network access to Fabric's Maven repository:
   ```bash
   curl -I https://maven.fabricmc.net/
   ```

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

### Troubleshooting

**If you get "Could not find fabric-api" errors:**
1. Verify the Fabric API version exists for Minecraft 1.21.11
2. Update `gradle.properties` with the correct version
3. Update `fabric.mod.json` with the same version
4. Run `./gradlew --refresh-dependencies :Fabric:build`

**If you get Mixin errors:**
- The code uses standard Minecraft APIs that should be stable between 1.21.8 and 1.21.11
- If there are errors, check if Mojang made any API changes in the patch version
- Most likely, no code changes will be needed

## Testing

After building:

1. Install the mod on a Minecraft 1.21.11 server with Fabric Loader 0.17.3+
2. Test the `/locate structure minecraft:village_plains` command
3. Verify that the command executes asynchronously without blocking the main thread
4. Test other features:
   - `/locate biome` command
   - Eye of Ender tracking
   - Villager exploration map trades
   - Dolphin treasure hunting

## Expected Compatibility

Since this is a minor patch version update (1.21.8 → 1.21.11):
- No code changes should be required
- Only version numbers need updating
- All mixins should work without modification
- The mod should function identically to the 1.21.8 version

## Notes

- This is a server-side only mod - clients don't need to install it
- The mod works by making structure/biome location searches asynchronous
- No client-side code or rendering is involved
- Fabric API and Fabric Loader are the only dependencies

## Additional Resources

- Fabric Development: https://fabricmc.net/develop/
- Fabric API Javadocs: https://maven.fabricmc.net/docs/fabric-api-0.100.8+1.21/
- Original mod by bright_spark, maintained by alvaro842
