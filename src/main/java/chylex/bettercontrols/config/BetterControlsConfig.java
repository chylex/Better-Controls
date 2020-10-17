package chylex.bettercontrols.config;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import net.minecraft.client.options.KeyBinding;
import java.nio.file.Path;

public final class BetterControlsConfig{
	public static BetterControlsConfig load(final Path path){
		return ConfigSerializer.deserialize(path).setPath(path);
	}
	
	private Path path;
	
	public boolean doubleTapForwardToSprint = true;
	public boolean resumeSprintingAfterHittingObstacle = false;
	
	public boolean sneakingMovesCameraSmoothly = true;
	
	public boolean flyOnGroundInCreative = false;
	public float flightSpeedMpCreativeDefault = 1F;
	public float flightSpeedMpCreativeSprinting = 2F;
	public float flightSpeedMpSpectatorDefault = 1F;
	public float flightSpeedMpSpectatorSprinting = 2F;
	
	public final KeyBindingWithModifier keyOpenMenu = new KeyBindingWithModifier("key.bettercontrols.open_menu");
	
	BetterControlsConfig(){}
	
	private BetterControlsConfig setPath(final Path path){
		this.path = path;
		return this;
	}
	
	public KeyBinding[] getAllKeyBindings(){
		return new KeyBinding[]{
			keyOpenMenu
		};
	}
	
	public void save(){
		ConfigSerializer.serialize(path, this);
	}
}
