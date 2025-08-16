package brightspark.asynclocator.mixins;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExplorationMapFunction.class)
public interface ExplorationMapFunctionAccessor {
	@Accessor("destination")
	TagKey<Structure> getDestination();

	@Accessor("zoom")
	byte getZoom();

	@Accessor("searchRadius")
	int getSearchRadius();

	@Accessor("skipKnownStructures")
	boolean isSkipKnownStructures();
}