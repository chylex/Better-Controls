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
			return isSprinting() ? cfg().flightSpeedMpCreativeSprinting : cfg().flightSpeedMpCreativeDefault;
		}
		else if (player.isSpectator()) {
			return isSprinting() ? cfg().flightSpeedMpSpectatorSprinting : cfg().flightSpeedMpSpectatorDefault;
		}
		else {
			return 1F;
		}
	}
	
	public static float getVerticalSpeedBoost(final LocalPlayer player) {
		if (player.isCreative()) {
			return isSprinting() ? cfg().flightVerticalBoostCreativeSprinting : cfg().flightVerticalBoostCreativeDefault;
		}
		else if (player.isSpectator()) {
			return isSprinting() ? cfg().flightVerticalBoostSpectatorSprinting : cfg().flightVerticalBoostSpectatorDefault;
		}
		else {
			return 0F;
		}
	}
}
