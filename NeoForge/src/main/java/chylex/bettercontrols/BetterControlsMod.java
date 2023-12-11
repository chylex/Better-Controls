package chylex.bettercontrols;

import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.gui.BetterControlsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.IExtensionPoint.DisplayTest;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.neoforged.neoforge.network.NetworkConstants;

@Mod("bettercontrols")
public final class BetterControlsMod {
	public BetterControlsMod() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			BetterControlsCommon.setConfig(BetterControlsConfig.load(FMLPaths.CONFIGDIR.get().resolve("BetterControls.json")));
			ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory(BetterControlsScreen::new));
		}
		
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}
}
