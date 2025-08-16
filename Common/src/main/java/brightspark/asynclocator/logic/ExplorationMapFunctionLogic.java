package brightspark.asynclocator.logic;

import brightspark.asynclocator.ALConstants;
import brightspark.asynclocator.AsyncLocator;
import brightspark.asynclocator.AsyncMapTracker;
import brightspark.asynclocator.platform.Services;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

import java.time.Duration;
import java.util.UUID;

public class ExplorationMapFunctionLogic {
	// I'd like to think that structure locating shouldn't take *this* long
    private static final Cache<UUID, Component> MAP_NAME_CACHE =
        CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(5)).build();

	private ExplorationMapFunctionLogic() {}

    public static void cacheName(ItemStack stack, Component name) {
        if (name == null) return;
        UUID id = CommonLogic.getTrackingUUID(stack);
        if (id != null) {
            MAP_NAME_CACHE.put(id, name);
            ALConstants.logDebug("Cached name for map UUID {}", id);
        } else {
            ALConstants.logWarn("Attempted to cache name for map without tracking UUID");
        }
    }

    public static Component getCachedName(ItemStack stack) {
        UUID id = CommonLogic.getTrackingUUID(stack);
        if (id == null) return null;
        Component name = MAP_NAME_CACHE.getIfPresent(id);
        if (name != null) {
            MAP_NAME_CACHE.invalidate(id);
            ALConstants.logDebug("Retrieved and invalidated cached name for map UUID {}", id);
        }
        return name;
    }

	private static void processLocationResult(UUID trackingId, BlockPos foundLocation) {
		AsyncMapTracker.getInstance().completeLocationOperation(trackingId, foundLocation);
	}

	public static void handleLocationFound(
		ItemStack mapStack,
		ServerLevel level,
		BlockPos pos,
		int scale,
		Holder<MapDecorationType> destinationTypeHolder,
		BlockPos invPos
	) {
		if (pos == null) {
			ALConstants.logInfo("No location found - invalidating map stack in inventory at {}", invPos);
			Services.EXPLORATION_MAP_FUNCTION_LOGIC.invalidateMap(mapStack, level, invPos);
		} else {
			ALConstants.logInfo("Location found at {} - updating treasure map in inventory at {}", pos, invPos);
			Component mapName = getCachedName(mapStack);
			Services.EXPLORATION_MAP_FUNCTION_LOGIC.updateMap(
				mapStack,
				level,
				pos,
				scale,
				destinationTypeHolder,
				invPos,
				mapName
			);
		}
	}

	public static ItemStack updateMapAsync(
		ServerLevel level,
		BlockPos blockPos,
		int scale,
		int searchRadius,
		boolean skipKnownStructures,
		Holder<MapDecorationType> destinationTypeHolder,
		TagKey<Structure> destination
	) {
		ItemStack mapStack = CommonLogic.createManagedMap();
		ALConstants.logDebug("Created managed map stack for async update (Search origin: {})", blockPos);

		// Extract UUID from the managed map for tracking
		var customData = mapStack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
		if (customData != null) {
			UUID operationId = customData.copyTag().getUUID("asynclocator.pending.uuid");
			
			boolean isChestGenerated = (level.getBlockEntity(blockPos) instanceof net.minecraft.world.level.block.entity.ChestBlockEntity);
			
			AsyncMapTracker.getInstance().addLocationOperation(operationId, 
				new AsyncMapTracker.MapLocationOperation(level.dimension(), scale, destinationTypeHolder));

			// Start the async location process
			AsyncLocator.locate(level, destination, blockPos, searchRadius, skipKnownStructures)
				.thenOnServerThread(foundPos -> {
					if (isChestGenerated) {
						handleLocationFound(mapStack, level, foundPos, scale, destinationTypeHolder, blockPos);
					} else {
						processLocationResult(operationId, foundPos);
					}
				});
		}
		
		return mapStack;
	}
}
