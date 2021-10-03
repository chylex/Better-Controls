package chylex.bettercontrols;
import chylex.bettercontrols.config.BetterControlsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class BetterControlsMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BetterControlsCommon.setConfig(BetterControlsConfig.load(FabricLoader.getInstance().getConfigDir().resolve("BetterControls.json")));
	}
}
