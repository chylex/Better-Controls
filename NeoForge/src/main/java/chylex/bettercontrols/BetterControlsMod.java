package chylex.bettercontrols;

import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.gui.BetterControlsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import javax.annotation.Nullable;

@Mod("bettercontrols")
public final class BetterControlsMod {
	public BetterControlsMod() {
		if (FMLEnvironment.getDist() == Dist.CLIENT) {
			BetterControlsCommon.setConfig(BetterControlsConfig.load(FMLPaths.CONFIGDIR.get().resolve("BetterControls.json")));
			ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> BetterControlsMod::createOptionsScreen);
		}
	}
	
	private static BetterControlsScreen createOptionsScreen(ModContainer modContainer, @Nullable Screen parentScreen) {
		return new BetterControlsScreen(parentScreen);
	}
}
