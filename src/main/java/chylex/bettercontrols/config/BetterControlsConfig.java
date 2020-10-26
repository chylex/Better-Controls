package chylex.bettercontrols.config;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import chylex.bettercontrols.input.SprintMode;
import net.minecraft.client.options.KeyBinding;
import java.nio.file.Path;

public final class BetterControlsConfig{
	public static BetterControlsConfig load(final Path path){
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
	public SprintMode sprintModeWhileFlying = SprintMode.TAP_TO_START;
	public boolean flyOnGroundInCreative = false;
	public float flightSpeedMpCreativeDefault = 1F;
	public float flightSpeedMpCreativeSprinting = 2F;
	public float flightSpeedMpSpectatorDefault = 1F;
	public float flightSpeedMpSpectatorSprinting = 2F;
	
	public final KeyBindingWithModifier keyToggleWalkForward = new KeyBindingWithModifier("key.bettercontrols.toggle_forward");
	public final KeyBindingWithModifier keyToggleJump = new KeyBindingWithModifier("key.bettercontrols.toggle_jump");
	public final KeyBindingWithModifier keyOpenMenu = new KeyBindingWithModifier("key.bettercontrols.open_menu");
	
	BetterControlsConfig(){}
	
	private BetterControlsConfig setPath(final Path path){
		this.path = path;
		return this;
	}
	
	public KeyBinding[] getAllKeyBindings(){
		return new KeyBinding[]{
			keyToggleSprint,
			keyToggleSneak,
			keyToggleFlight,
			keyToggleWalkForward,
			keyToggleJump,
			keyOpenMenu
		};
	}
	
	public void save(){
		ConfigSerializer.serialize(path, this);
	}
}
