package chylex.bettercontrols;
import chylex.bettercontrols.config.BetterControlsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class BetterControlsMod implements ClientModInitializer {
	public static final BetterControlsConfig config = BetterControlsConfig.load(FabricLoader.getInstance().getConfigDir().resolve("BetterControls.json"));
	
	@Override
	public void onInitializeClient() {}
}
