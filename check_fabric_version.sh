#!/bin/bash
# Script to verify and find the correct Fabric API version for Minecraft 1.21.11

set -e

MC_VERSION="1.21.11"
echo "Checking Fabric API versions for Minecraft ${MC_VERSION}..."

# Try to fetch available versions from Fabric Maven
echo ""
echo "Attempting to fetch version information from Fabric Maven..."

MAVEN_METADATA_URL="https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/maven-metadata.xml"

if command -v curl &> /dev/null; then
    echo "Using curl to fetch version data..."
    if curl -s --fail "${MAVEN_METADATA_URL}" > /tmp/fabric-versions.xml 2>/dev/null; then
        echo "Successfully fetched version metadata!"
        echo ""
        echo "Available Fabric API versions for Minecraft ${MC_VERSION}:"
        grep -oP "\d+\.\d+\.\d+\+${MC_VERSION}" /tmp/fabric-versions.xml | sort -V | tail -20 || echo "No versions found for ${MC_VERSION}"
        
        # Get the latest version
        LATEST_VERSION=$(grep -oP "\d+\.\d+\.\d+\+${MC_VERSION}" /tmp/fabric-versions.xml | sort -V | tail -1)
        
        if [ -n "$LATEST_VERSION" ]; then
            echo ""
            echo "Latest version found: ${LATEST_VERSION}"
            echo ""
            echo "To update your project, run:"
            echo "  sed -i 's/fabric_version.*=.*/fabric_version        = ${LATEST_VERSION}/' gradle.properties"
            echo "  sed -i 's/\"fabric-api\": \">=.*\+${MC_VERSION}\"/\"fabric-api\": \">=${LATEST_VERSION}\"/' Fabric/src/main/resources/fabric.mod.json"
        else
            echo "⚠️  No Fabric API version found for Minecraft ${MC_VERSION}"
            echo "    This might mean:"
            echo "    1. Minecraft ${MC_VERSION} is not yet supported by Fabric"
            echo "    2. There's a network issue"
            echo "    3. The version number is incorrect"
            echo ""
            echo "    Please check https://fabricmc.net/develop/ manually"
        fi
    else
        echo "❌ Failed to fetch version metadata from Fabric Maven"
        echo "   Please check your network connection and try again"
        echo "   Or visit https://fabricmc.net/develop/ manually"
        exit 1
    fi
else
    echo "❌ curl is not installed. Please install curl or check https://fabricmc.net/develop/ manually"
    exit 1
fi

echo ""
echo "After updating, build with: ./gradlew :Fabric:build"
