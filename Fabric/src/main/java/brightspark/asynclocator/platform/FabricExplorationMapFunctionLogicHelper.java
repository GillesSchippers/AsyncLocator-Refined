package brightspark.asynclocator.platform;

import brightspark.asynclocator.ALConstants;
import brightspark.asynclocator.logic.CommonLogic;
import brightspark.asynclocator.platform.services.ExplorationMapFunctionLogicHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class FabricExplorationMapFunctionLogicHelper implements ExplorationMapFunctionLogicHelper {
	@Override
	public void invalidateMap(ItemStack mapStack, ServerLevel level, BlockPos invPos) {
		handleUpdateMapInChest(mapStack, level, invPos, (chest, slot) -> {
			ALConstants.logDebug("Invalidating map in Fabric inventory slot {}", slot);
			chest.setItem(slot, new ItemStack(Items.MAP));
		});
	}

	@Override
	public void updateMap(
		ItemStack mapStack,
		ServerLevel level,
		BlockPos pos,
		int scale,
		Holder<MapDecorationType> destinationTypeHolder,
		BlockPos invPos,
		@Nullable Component displayName
	) {
		CommonLogic.finalizeMap(mapStack, level, pos, scale, destinationTypeHolder, displayName);

		handleUpdateMapInChest(mapStack, level, invPos, (chest, slot) -> {
			ALConstants.logDebug("Updated map in Fabric inventory slot {}, broadcasting changes.", slot);
			chest.setItem(slot, mapStack);
		});
	}

	private static void handleUpdateMapInChest(
		ItemStack mapStackToFind,
		ServerLevel level,
		BlockPos inventoryPos,
		BiConsumer<ChestBlockEntity, Integer> handleSlotFound
	) {
		BlockEntity be = level.getBlockEntity(inventoryPos);
		if (be instanceof ChestBlockEntity chest) {
			boolean found = false;
            java.util.UUID targetId = brightspark.asynclocator.logic.CommonLogic.getTrackingUUID(mapStackToFind);
            for (int i = 0; i < chest.getContainerSize(); i++) {
                ItemStack slotStack = chest.getItem(i);
                java.util.UUID slotId = brightspark.asynclocator.logic.CommonLogic.getTrackingUUID(slotStack);
                if (targetId != null && targetId.equals(slotId)) {
					handleSlotFound.accept(chest, i);
					CommonLogic.broadcastChestChanges(level, be);
					found = true;
					break;
				}
			}
			if (!found) {
				ALConstants.logWarn("Could not find the specific map ItemStack instance in ChestBE at {}", inventoryPos);
			}
		} else {
			ALConstants.logWarn(
				"No ChestBlockEntity at inventory position {} in level {}",
				inventoryPos, level.dimension().location()
			);
		}
	}
}
