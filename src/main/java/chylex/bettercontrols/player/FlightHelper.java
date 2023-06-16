package chylex.bettercontrols.player;

import chylex.bettercontrols.BetterControlsCommon;
import chylex.bettercontrols.config.BetterControlsConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class FlightHelper {
	private FlightHelper() {}
	
	private static final KeyMapping KEY_SPRINT = Minecraft.getInstance().options.keySprint;
	
	private static boolean isSprinting() {
		return KEY_SPRINT.isDown();
	}
	
	private static BetterControlsConfig cfg() {
		return BetterControlsCommon.getConfig();
	}
	
	static boolean isFlyingCreativeOrSpectator(final LocalPlayer player) {
		return player.getAbilities().flying && (player.isCreative() || player.isSpectator());
	}
	
	static boolean shouldFlyOnGround(final LocalPlayer player) {
		return cfg().flyOnGroundInCreative && player.isCreative() && player.getAbilities().flying;
	}
	
	public static float getHorizontalSpeedMultiplier(final LocalPlayer player) {
		if (player.isCreative()) {
			return isSprinting() ? cfg().flightHorizontalSpeedMpCreativeSprinting : cfg().flightHorizontalSpeedMpCreativeDefault;
		}
		else if (player.isSpectator()) {
			return isSprinting() ? cfg().flightHorizontalSpeedMpSpectatorSprinting : cfg().flightHorizontalSpeedMpSpectatorDefault;
		}
		else {
			return 1F;
		}
	}
	
	public static float getVerticalSpeedMultiplier(final LocalPlayer player) {
		if (player.isCreative()) {
			return isSprinting() ? cfg().flightVerticalSpeedMpCreativeSprinting : cfg().flightVerticalSpeedMpCreativeDefault;
		}
		else if (player.isSpectator()) {
			return isSprinting() ? cfg().flightVerticalSpeedMpSpectatorSprinting : cfg().flightVerticalSpeedMpSpectatorDefault;
		}
		else {
			return 1F;
		}
	}
}
