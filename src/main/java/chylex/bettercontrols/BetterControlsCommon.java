package chylex.bettercontrols;

import chylex.bettercontrols.config.BetterControlsConfig;

public final class BetterControlsCommon {
	private static BetterControlsConfig config;
	
	public static BetterControlsConfig getConfig() {
		return config;
	}
	
	static void setConfig(BetterControlsConfig config) {
		BetterControlsCommon.config = config;
	}
	
	private BetterControlsCommon() {}
}
