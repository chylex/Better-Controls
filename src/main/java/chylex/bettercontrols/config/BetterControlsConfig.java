package chylex.bettercontrols.config;

import chylex.bettercontrols.input.KeyBindingWithModifier;
import chylex.bettercontrols.input.SprintMode;
import java.nio.file.Path;

public final class BetterControlsConfig {
	public static BetterControlsConfig load(final Path path) {
		return ConfigSerializer.deserialize(path).setPath(path);
	}
	
	private Path path;
	
	public final KeyBindingWithModifier keyToggleSprint = new KeyBindingWithModifier("key.bettercontrols.toggle_sprint");
	public SprintMode sprintMode = SprintMode.TAP_TO_START;
	public boolean doubleTapForwardToSprint = true;
	public boolean resumeSprintingAfterHittingObstacle = false;
	
	public final KeyBindingWithModifier keyToggleSneak = new KeyBindingWithModifier("key.bettercontrols.toggle_sneak");
	public boolean sneakingMovesCameraSmoothly = true;
	
	public final KeyBindingWithModifier keyToggleFlight = new KeyBindingWithModifier("key.bettercontrols.toggle_flight");
	public boolean doubleTapJumpToToggleFlight = true;
	public boolean disableFlightInertia = false;
	public boolean disableChangingFovWhileFlying = false;
	public boolean flyOnGroundInCreative = false;
	public float flightHorizontalSpeedMpCreativeDefault = 1F;
	public float flightHorizontalSpeedMpCreativeSprinting = 2F;
	public float flightHorizontalSpeedMpSpectatorDefault = 1F;
	public float flightHorizontalSpeedMpSpectatorSprinting = 2F;
	public float flightVerticalSpeedMpCreativeDefault = 1F;
	public float flightVerticalSpeedMpCreativeSprinting = 2F;
	public float flightVerticalSpeedMpSpectatorDefault = 1F;
	public float flightVerticalSpeedMpSpectatorSprinting = 2F;
	
	public final KeyBindingWithModifier keyToggleWalkForward = new KeyBindingWithModifier("key.bettercontrols.toggle_forward");
	public final KeyBindingWithModifier keyToggleJump = new KeyBindingWithModifier("key.bettercontrols.toggle_jump");
	public final KeyBindingWithModifier keyResetAllToggles = new KeyBindingWithModifier("key.bettercontrols.reset_all_toggles");
	public final KeyBindingWithModifier keyOpenMenu = new KeyBindingWithModifier("key.bettercontrols.open_menu");
	
	BetterControlsConfig() {}
	
	private BetterControlsConfig setPath(final Path path) {
		this.path = path;
		return this;
	}
	
	public KeyBindingWithModifier[] getAllKeyBindings() {
		return new KeyBindingWithModifier[] {
			keyToggleSprint,
			keyToggleSneak,
			keyToggleFlight,
			keyToggleWalkForward,
			keyToggleJump,
			keyResetAllToggles,
			keyOpenMenu
		};
	}
	
	public void save() {
		ConfigSerializer.serialize(path, this);
	}
}
