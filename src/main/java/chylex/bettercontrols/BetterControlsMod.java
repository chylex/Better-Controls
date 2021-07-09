package chylex.bettercontrols;
import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.gui.BetterControlsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod("bettercontrols")
public final class BetterControlsMod{
	public static final BetterControlsConfig config = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientLoader::loadConfig);
	
	public BetterControlsMod(){
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> ClientLoader::createScreen);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
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
