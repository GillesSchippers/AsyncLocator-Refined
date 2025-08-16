package brightspark.asynclocator.logic;

import brightspark.asynclocator.ALConstants;
import brightspark.asynclocator.AsyncLocator;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.EnderEyeItem;

public class EnderEyeItemLogic {
	private EnderEyeItemLogic() {}

	public static void locateAsync(ServerLevel level, Player player, EyeOfEnder eyeOfEnder, EnderEyeItem enderEyeItem) {
		AsyncLocator.locate(
			level,
			StructureTags.EYE_OF_ENDER_LOCATED,
			player.blockPosition(),
			100,
			false
		).thenOnServerThread(pos -> {
			((EyeOfEnderData) eyeOfEnder).setLocateTaskOngoing(false);
			if (pos != null) {
				ALConstants.logInfo("Location found - updating eye of ender entity");
				eyeOfEnder.signalTo(pos);
				if (player instanceof ServerPlayer sp) {
					CriteriaTriggers.USED_ENDER_EYE.trigger(sp, pos);
				}
				player.awardStat(Stats.ITEM_USED.get(enderEyeItem));
			} else {
				ALConstants.logInfo("No location found - removing eye of ender entity");
				eyeOfEnder.discard();
			}
		});
		((EyeOfEnderData) eyeOfEnder).setLocateTaskOngoing(true);
	}
}
