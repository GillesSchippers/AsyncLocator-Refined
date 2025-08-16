package brightspark.asynclocator.platform;

import brightspark.asynclocator.ALConstants;
import brightspark.asynclocator.logic.CommonLogic;
import brightspark.asynclocator.platform.services.ExplorationMapFunctionLogicHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ForgeExplorationMapFunctionLogicHelper implements ExplorationMapFunctionLogicHelper {
	@Override
	public void invalidateMap(ItemStack mapStack, ServerLevel level, BlockPos invPos) {
		handleUpdateMapInChest(mapStack, level, invPos, (handler, slot) -> {
			ALConstants.logDebug("Invalidating map in Forge inventory slot {}", slot);
			if (handler instanceof IItemHandlerModifiable modifiableHandler) {
				modifiableHandler.setStackInSlot(slot, new ItemStack(Items.MAP));
			} else {
				ItemStack extracted = handler.extractItem(slot, mapStack.getCount(), false);
				if (!extracted.isEmpty()) {
				handler.insertItem(slot, new ItemStack(Items.MAP), false);
				}
			}
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
		
		handleUpdateMapInChest(mapStack, level, invPos, (handler, slot) -> {
			ALConstants.logDebug("Updated map in Forge inventory slot {}, broadcasting changes.", slot);
			if (handler instanceof IItemHandlerModifiable modifiableHandler) {
				modifiableHandler.setStackInSlot(slot, mapStack);
			} else {
				handler.extractItem(slot, mapStack.getCount(), false);
				handler.insertItem(slot, mapStack, false);
			}
		});
	}

	private static void handleUpdateMapInChest(
		ItemStack mapStackToFind,
		ServerLevel level,
		BlockPos inventoryPos,
		BiConsumer<IItemHandler, Integer> handleSlotFound
	) {
		BlockEntity be = level.getBlockEntity(inventoryPos);
		if (be != null) {
			be.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().ifPresentOrElse(
				itemHandler -> {
					boolean found = false;
					for (int i = 0; i < itemHandler.getSlots(); i++) {
						ItemStack slotStack = itemHandler.getStackInSlot(i);
						if (slotStack == mapStackToFind) {
							handleSlotFound.accept(itemHandler, i);
							CommonLogic.broadcastChestChanges(level, be);
							found = true;
							break;
						}
					}
					if (!found) {
						ALConstants.logWarn("Could not find the specific map ItemStack instance in {} at {}", be.getClass().getSimpleName(), inventoryPos);
					}
				},
				() -> ALConstants.logWarn(
					"Couldn't find item handler capability on block entity {} at {}",
					be.getClass().getSimpleName(), inventoryPos
				)
			);
		} else {
			ALConstants.logWarn(
				"Couldn't find block entity at inventory position {} in level {}",
				inventoryPos, level.dimension().location()
			);
		}
	}
}
