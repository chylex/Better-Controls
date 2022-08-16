package chylex.bettercontrols;
import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.gui.BetterControlsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;

@Mod("bettercontrols")
public final class BetterControlsMod {
	public BetterControlsMod() {
		BetterControlsCommon.setConfig(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientLoader::loadConfig));
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory(ClientLoader::createScreen));
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}
	
	private static final class ClientLoader {
		public static BetterControlsConfig loadConfig() {
			return BetterControlsConfig.load(FMLPaths.CONFIGDIR.get().resolve("BetterControls.json"));
		}
		
		public static BetterControlsScreen createScreen(final Minecraft mc, final Screen parentScreen) {
			return new BetterControlsScreen(parentScreen);
		}
	}
}
