package chylex.bettercontrols.player;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import static chylex.bettercontrols.BetterControlsCommon.getConfig;

public final class FlightHelper {
	private FlightHelper() {}
	
	private static final KeyMapping KEY_SPRINT = Minecraft.getInstance().options.keySprint;
	
	private static boolean isSprinting() {
		return KEY_SPRINT.isDown();
	}
	
	public static boolean shouldStartGliding(boolean isHoldingJump) {
		return getConfig().keyStartGlide.isDown() || (getConfig().doubleTapJumpToGlide && isHoldingJump);
	}
	
	public static boolean isFlyingCreativeOrSpectator(LocalPlayer player) {
		return player.getAbilities().flying && (player.isCreative() || player.isSpectator());
	}
	
	static boolean shouldFlyOnGround(LocalPlayer player) {
		return getConfig().flyOnGroundInCreative && player.isCreative() && player.getAbilities().flying;
	}
	
	public static float getHorizontalSpeedMultiplier(LocalPlayer player) {
		if (player.isCreative()) {
			return isSprinting() ? getConfig().flightHorizontalSpeedMpCreativeSprinting : getConfig().flightHorizontalSpeedMpCreativeDefault;
		}
		else if (player.isSpectator()) {
			return isSprinting() ? getConfig().flightHorizontalSpeedMpSpectatorSprinting : getConfig().flightHorizontalSpeedMpSpectatorDefault;
		}
		else {
			return 1F;
		}
	}
	
	public static float getVerticalSpeedMultiplier(LocalPlayer player) {
		if (player.isCreative()) {
			return isSprinting() ? getConfig().flightVerticalSpeedMpCreativeSprinting : getConfig().flightVerticalSpeedMpCreativeDefault;
		}
		else if (player.isSpectator()) {
			return isSprinting() ? getConfig().flightVerticalSpeedMpSpectatorSprinting : getConfig().flightVerticalSpeedMpSpectatorDefault;
		}
		else {
			return 1F;
		}
	}
}
