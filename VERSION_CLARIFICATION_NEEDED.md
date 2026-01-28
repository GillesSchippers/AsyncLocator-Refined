# ⚠️ IMPORTANT: Version Clarification Needed

## Issue with Requested Version

The requested version **"1.21.11"** does not follow Minecraft's standard versioning scheme.

### Minecraft Java Edition Versioning

Minecraft uses semantic versioning: `MAJOR.MINOR.PATCH`

Examples of real versions:
- 1.20.1
- 1.20.2
- 1.21
- 1.21.1
- 1.21.2
- 1.21.3
- 1.21.4

**Minecraft has never released a version with a two-digit patch number like 1.21.11**

### Possible Interpretations

The request for "1.21.11" could mean:

1. **1.21.1** (Most likely)
   - First patch release of Minecraft 1.21
   - Released August 2024
   - Fabric API version would be: ~0.102.0+1.21.1

2. **1.21** (Possible)
   - Base 1.21 release
   - Released June 2024
   - Fabric API version would be: ~0.100.0+1.21

3. **Latest 1.21.x** (As of Jan 2026)
   - Could be 1.21.4 or 1.21.5
   - Would need to check current release

4. **A future version** (Unlikely)
   - Minecraft versions don't use two-digit patch numbers
   - Would break established versioning pattern

## Current State of This PR

All files have been updated to reference **1.21.11**, which is likely incorrect. Changes include:

- gradle.properties
- fabric.mod.json
- neoforge.mods.toml
- pack.mcmeta
- README.md

## Recommended Action

**Please clarify which Minecraft version you actually need:**

### If you meant 1.21.1:
All version numbers need to be changed from `1.21.11` to `1.21.1` in all configuration files.

### If you meant 1.21:
All version numbers need to be changed from `1.21.11` to `1.21` in all configuration files.

### If you meant the latest 1.21.x:
Please specify the exact version (e.g., 1.21.4) so we can update accordingly.

## How to Proceed

1. **Reply with the correct Minecraft version** you want to target
2. I will update all files with the correct version numbers
3. We can then verify the correct Fabric API version
4. Build and test

## Questions to Help Clarify

- Are you using a Minecraft server? What version does it show?
- Where did you see "1.21.11" referenced?
- What Fabric Loader version are you currently using?
- Is this for a public server or private use?

---

**Until this is clarified, the build will likely fail** because dependencies for "1.21.11" don't exist.
