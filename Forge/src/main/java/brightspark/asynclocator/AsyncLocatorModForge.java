package brightspark.asynclocator;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ALConstants.MOD_ID)
public class AsyncLocatorModForge {
	public AsyncLocatorModForge(IEventBus modEventBus) {
		ModLoadingContext ctx = ModLoadingContext.get();

		ctx.registerExtensionPoint(
			IExtensionPoint.DisplayTest.class,
			() -> new IExtensionPoint.DisplayTest(
				() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY,
				(serverVersion, networkBool) -> true
			)
		);

		ctx.registerConfig(ModConfig.Type.SERVER, AsyncLocatorConfigForge.SPEC);

		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener((ModConfigEvent.Loading event) -> AsyncLocatorModCommon.printConfigs());

		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
		forgeEventBus.addListener((ServerAboutToStartEvent event) -> AsyncLocator.setupExecutorService());
		forgeEventBus.addListener((ServerStoppingEvent event) -> AsyncLocator.shutdownExecutorService());
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(ALDataComponents::init);
	}
}
