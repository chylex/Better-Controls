package chylex.bettercontrols.player;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.gui.BetterControlsScreen;
import chylex.bettercontrols.input.SprintMode;
import chylex.bettercontrols.input.ToggleTracker;
import chylex.bettercontrols.input.ToggleTrackerForStickyKey;
import chylex.bettercontrols.mixin.AccessCameraFields;
import chylex.bettercontrols.mixin.AccessClientPlayerFields;
import chylex.bettercontrols.mixin.AccessGameRendererFields;
import chylex.bettercontrols.mixin.AccessStickyKeyBindingStateGetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.util.math.MathHelper;
import java.lang.ref.WeakReference;
import java.util.function.BooleanSupplier;

public final class PlayerTicker{
	private static PlayerTicker ticker = new PlayerTicker(null);
	
	public static PlayerTicker get(final ClientPlayerEntity player){
		if (ticker.ref.get() != player){
			ticker = new PlayerTicker(player);
		}
		
		return ticker;
	}
	
	private static MinecraftClient mc(){
		return MinecraftClient.getInstance();
	}
	
	private static BetterControlsConfig cfg(){
		return BetterControlsMod.config;
	}
	
	private final WeakReference<ClientPlayerEntity> ref;
	
	private PlayerTicker(final ClientPlayerEntity player){
		this.ref = new WeakReference<>(player);
		setup();
	}
	
	// Logic
	
	private final ToggleTracker toggleSprint = new ToggleTrackerForStickyKey(cfg().keyToggleSprint, mc().options.keySprint, toggled -> mc().options.sprintToggled = toggled);
	private final ToggleTracker toggleSneak = new ToggleTrackerForStickyKey(cfg().keyToggleSneak, mc().options.keySneak, toggled -> mc().options.sneakToggled = toggled);
	private final ToggleTracker toggleWalkForward = new ToggleTracker(cfg().keyToggleWalkForward, mc().options.keyForward);
	private final ToggleTracker toggleJump = new ToggleTracker(cfg().keyToggleJump, mc().options.keyJump);
	
	private boolean waitingForSprintKeyRelease = false;
	private boolean stopSprintingAfterReleasingSprintKey = false;
	
	private boolean wasHittingObstacle = false;
	private boolean wasSprintingBeforeHittingObstacle = false;
	private int temporarySprintTimer = 0;
	
	private boolean wasSneakingBeforeTouchingGround = false;
	private boolean holdingSneakWhileTouchingGround = false;
	private int temporaryFlyOnGroundTimer = 0;
	
	private void setup(){
		final AccessStickyKeyBindingStateGetter sprint = (AccessStickyKeyBindingStateGetter)mc().options.keySprint;
		BooleanSupplier getter = sprint.getToggleGetter();
		
		if (getter instanceof SprintPressGetter){
			getter = ((SprintPressGetter)getter).getWrapped();
		}
		
		sprint.setToggleGetter(new SprintPressGetter(getter, () -> temporarySprintTimer > 0));
	}
	
	public void atHead(final ClientPlayerEntity player){
		if (FlightHelper.shouldFlyOnGround(player)){
			player.onGround = false;
		}
		
		if (!cfg().doubleTapForwardToSprint){
			((AccessClientPlayerFields)player).setTicksLeftToDoubleTapSprint(0);
		}
		
		final SprintMode sprintMode;
		
		if (FlightHelper.isFlyingCreativeOrSpectator(player)){
			sprintMode = cfg().sprintModeWhileFlying;
		}
		else{
			sprintMode = cfg().sprintMode;
		}
		
		final GameOptions opts = mc().options;
		final boolean wasSprintToggled = opts.sprintToggled;
		final boolean isSprintToggled = toggleSprint.tick();
		
		if (temporarySprintTimer > 0){
			stopSprintingAfterReleasingSprintKey = false;
			waitingForSprintKeyRelease = false;
			
			final int nextTemporarySprintTimer = temporarySprintTimer - 1;
			temporarySprintTimer = 0;
			
			if (!opts.keySprint.isPressed() && opts.keyForward.isPressed()){
				temporarySprintTimer = nextTemporarySprintTimer;
			}
			else if (sprintMode == SprintMode.TAP_TO_TOGGLE){
				stopSprintingAfterReleasingSprintKey = true;
			}
		}
		
		if (isSprintToggled){
			stopSprintingAfterReleasingSprintKey = false;
			waitingForSprintKeyRelease = false;
		}
		else if (wasSprintToggled){
			stopSprintingAfterReleasingSprintKey = true;
			waitingForSprintKeyRelease = true;
		}
		else if (sprintMode == SprintMode.TAP_TO_TOGGLE){
			if (opts.keySprint.isPressed()){
				if (!waitingForSprintKeyRelease){
					waitingForSprintKeyRelease = true;
					stopSprintingAfterReleasingSprintKey = player.isSprinting();
				}
			}
			else{
				if (player.isSprinting() && !waitingForSprintKeyRelease){
					stopSprintingAfterReleasingSprintKey = false;
				}
				
				waitingForSprintKeyRelease = false;
			}
		}
		else if (sprintMode == SprintMode.HOLD){
			if (opts.keySprint.isPressed()){
				stopSprintingAfterReleasingSprintKey = true;
			}
		}
		
		if (stopSprintingAfterReleasingSprintKey && !opts.keySprint.isPressed()){
			stopSprintingAfterReleasingSprintKey = false;
			waitingForSprintKeyRelease = false;
			player.setSprinting(false);
		}
		
		toggleSneak.tick();
	}
	
	public void afterInputAssignsPressingForward(final KeyboardInput input){
		if (mc().currentScreen == null){
			input.pressingForward |= toggleWalkForward.tick();
		}
	}
	
	public void afterInputTick(final ClientPlayerEntity player){
		if (mc().currentScreen == null && !player.abilities.flying){
			player.input.jumping |= toggleJump.tick();
		}
		
		if (FlightHelper.isFlyingCreativeOrSpectator(player)){
			final float flightSpeed = FlightHelper.getFlightSpeed(player);
			
			if (flightSpeed > 0F){
				player.abilities.setFlySpeed(flightSpeed);
			}
			
			final float verticalVelocity = FlightHelper.getExtraVerticalVelocity(player);
			
			if (!MathHelper.approximatelyEquals(verticalVelocity, 0F) && player == mc().getCameraEntity()){
				int direction = 0;
				
				if (player.input.sneaking){
					--direction;
				}
				
				if (player.input.jumping){
					++direction;
				}
				
				if (direction != 0){
					player.setVelocity(player.getVelocity().add(0D, flightSpeed * verticalVelocity * direction, 0D));
				}
			}
		}
		
		if (cfg().resumeSprintingAfterHittingObstacle){
			if (wasHittingObstacle != player.horizontalCollision){
				if (!wasHittingObstacle){
					wasSprintingBeforeHittingObstacle = player.isSprinting() || mc().options.keySprint.isPressed();
				}
				else if (wasSprintingBeforeHittingObstacle){
					wasSprintingBeforeHittingObstacle = false;
					temporarySprintTimer = 10;
				}
				
				// collision also stops when the player lets go of movement keys
				wasHittingObstacle = player.horizontalCollision;
			}
		}
		else{
			wasHittingObstacle = player.horizontalCollision;
			wasSprintingBeforeHittingObstacle = false;
		}
		
		if (cfg().disableChangingFovWhileFlying && FlightHelper.isFlyingCreativeOrSpectator(player)){
			((AccessGameRendererFields)mc().gameRenderer).setMovementFovMultiplier(1F);
		}
	}
	
	public void afterSuperCall(final ClientPlayerEntity player){
		if (FlightHelper.shouldFlyOnGround(player)){
			final boolean isSneaking = player.isSneaking();
			final boolean isOnGround = player.onGround;
			
			if (!isSneaking){
				wasSneakingBeforeTouchingGround = false;
			}
			else if (!isOnGround){
				wasSneakingBeforeTouchingGround = true;
			}
			
			if (!isOnGround){
				holdingSneakWhileTouchingGround = false;
			}
			else{
				boolean cancelLanding = true;
				
				if (!wasSneakingBeforeTouchingGround){
					if (isSneaking){
						holdingSneakWhileTouchingGround = true;
					}
					else if (holdingSneakWhileTouchingGround){
						player.abilities.flying = false;
						player.sendAbilitiesUpdate();
						cancelLanding = false;
					}
				}
				
				if (cancelLanding){
					player.onGround = false;
				}
			}
		}
		else{
			wasSneakingBeforeTouchingGround = false;
			holdingSneakWhileTouchingGround = false;
		}
		
		if (player.isCreative()){
			if (cfg().keyToggleFlight.wasPressed()){
				final boolean isFlying = !player.abilities.flying;
				
				player.abilities.flying = isFlying;
				player.sendAbilitiesUpdate();
				
				if (isFlying){
					temporaryFlyOnGroundTimer = 10;
				}
			}
			
			if (temporaryFlyOnGroundTimer > 0){
				if (player.isSneaking()){
					temporaryFlyOnGroundTimer = 0;
				}
				else{
					--temporaryFlyOnGroundTimer;
					player.onGround = false;
				}
			}
		}
		else{
			temporaryFlyOnGroundTimer = 0;
		}
		
		if (!cfg().sneakingMovesCameraSmoothly){
			final AccessCameraFields camera = (AccessCameraFields)mc().gameRenderer.getCamera();
			
			if (camera.getFocusedEntity() == player){
				camera.setCameraY(player.getStandingEyeHeight());
			}
		}
		
		if (cfg().keyResetAllToggles.wasPressed()){
			toggleSprint.reset();
			toggleSneak.reset();
			toggleWalkForward.reset();
			toggleJump.reset();
		}
		
		if (cfg().keyOpenMenu.isPressed()){
			mc().openScreen(new BetterControlsScreen(null));
		}
	}
}
