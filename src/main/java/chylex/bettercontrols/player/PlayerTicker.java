package chylex.bettercontrols.player;
import chylex.bettercontrols.BetterControlsCommon;
import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.gui.BetterControlsScreen;
import chylex.bettercontrols.input.SprintMode;
import chylex.bettercontrols.input.ToggleTracker;
import chylex.bettercontrols.input.ToggleTrackerForStickyKey;
import chylex.bettercontrols.mixin.AccessCameraFields;
import chylex.bettercontrols.mixin.AccessClientPlayerFields;
import chylex.bettercontrols.mixin.AccessPlayerFields;
import chylex.bettercontrols.mixin.AccessStickyKeyBindingStateGetter;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import java.lang.ref.WeakReference;
import java.util.function.BooleanSupplier;

public final class PlayerTicker {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();
	private static final Options OPTIONS = MINECRAFT.options;
	
	private static final KeyMapping KEY_SPRINT = OPTIONS.keySprint;
	private static final KeyMapping KEY_SNEAK = OPTIONS.keyShift;
	private static final KeyMapping KEY_FORWARD = OPTIONS.keyUp;
	private static final KeyMapping KEY_JUMP = OPTIONS.keyJump;
	
	private static PlayerTicker ticker = new PlayerTicker(null);
	
	public static PlayerTicker get(final LocalPlayer player) {
		if (ticker.ref.get() != player) {
			ticker = new PlayerTicker(player);
		}
		
		return ticker;
	}
	
	private static BetterControlsConfig cfg() {
		return BetterControlsCommon.getConfig();
	}
	
	private final WeakReference<LocalPlayer> ref;
	
	private PlayerTicker(final LocalPlayer player) {
		this.ref = new WeakReference<>(player);
		setup();
	}
	
	// Logic
	
	private final ToggleTracker toggleSprint = new ToggleTrackerForStickyKey(cfg().keyToggleSprint, KEY_SPRINT, OPTIONS.toggleSprint()::set);
	private final ToggleTracker toggleSneak = new ToggleTrackerForStickyKey(cfg().keyToggleSneak, KEY_SNEAK, OPTIONS.toggleCrouch()::set);
	private final ToggleTracker toggleWalkForward = new ToggleTracker(cfg().keyToggleWalkForward, KEY_FORWARD);
	private final ToggleTracker toggleJump = new ToggleTracker(cfg().keyToggleJump, KEY_JUMP);
	
	private boolean waitingForSprintKeyRelease = false;
	private boolean stopSprintingAfterReleasingSprintKey = false;
	
	private boolean wasHittingObstacle = false;
	private boolean wasSprintingBeforeHittingObstacle = false;
	private int temporarySprintTimer = 0;
	
	private boolean wasSneakingBeforeTouchingGround = false;
	private boolean holdingSneakWhileTouchingGround = false;
	private int temporaryFlyOnGroundTimer = 0;
	
	private void setup() {
		final AccessStickyKeyBindingStateGetter sprint = (AccessStickyKeyBindingStateGetter)KEY_SPRINT;
		BooleanSupplier getter = sprint.getNeedsToggle();
		
		if (getter instanceof final SprintPressGetter g) {
			getter = g.wrapped();
		}
		
		sprint.setNeedsToggle(new SprintPressGetter(getter, () -> temporarySprintTimer > 0));
	}
	
	public void atHead(final LocalPlayer player) {
		if (FlightHelper.shouldFlyOnGround(player)) {
			player.setOnGround(false);
		}
		
		if (!cfg().doubleTapForwardToSprint) {
			((AccessClientPlayerFields)player).setSprintTriggerTime(0);
		}
		
		if (!cfg().doubleTapJumpToToggleFlight) {
			((AccessPlayerFields)player).setJumpTriggerTime(0);
		}
		
		final SprintMode sprintMode = cfg().sprintMode;
		final boolean wasSprintToggled = Boolean.TRUE.equals(OPTIONS.toggleSprint().get());
		final boolean isSprintToggled = toggleSprint.tick();
		
		if (temporarySprintTimer > 0) {
			stopSprintingAfterReleasingSprintKey = false;
			waitingForSprintKeyRelease = false;
			
			final int nextTemporarySprintTimer = temporarySprintTimer - 1;
			temporarySprintTimer = 0;
			
			if (!KEY_SPRINT.isDown() && KEY_FORWARD.isDown()) {
				temporarySprintTimer = nextTemporarySprintTimer;
			}
			else if (sprintMode == SprintMode.TAP_TO_TOGGLE) {
				stopSprintingAfterReleasingSprintKey = true;
			}
		}
		
		if (isSprintToggled) {
			stopSprintingAfterReleasingSprintKey = false;
			waitingForSprintKeyRelease = false;
		}
		else if (wasSprintToggled) {
			stopSprintingAfterReleasingSprintKey = true;
			waitingForSprintKeyRelease = true;
		}
		else if (sprintMode == SprintMode.TAP_TO_TOGGLE) {
			if (KEY_SPRINT.isDown()) {
				if (!waitingForSprintKeyRelease) {
					waitingForSprintKeyRelease = true;
					stopSprintingAfterReleasingSprintKey = player.isSprinting();
				}
			}
			else {
				if (player.isSprinting() && !waitingForSprintKeyRelease) {
					stopSprintingAfterReleasingSprintKey = false;
				}
				
				waitingForSprintKeyRelease = false;
			}
		}
		else if (sprintMode == SprintMode.HOLD) {
			if (KEY_SPRINT.isDown()) {
				stopSprintingAfterReleasingSprintKey = true;
			}
		}
		
		if (stopSprintingAfterReleasingSprintKey && !KEY_SPRINT.isDown()) {
			stopSprintingAfterReleasingSprintKey = false;
			waitingForSprintKeyRelease = false;
			player.setSprinting(false);
		}
		
		toggleSneak.tick();
	}
	
	public void afterInputAssignsPressingForward(final Input input) {
		if (MINECRAFT.screen == null) {
			input.up |= toggleWalkForward.tick();
		}
	}
	
	public void afterInputTick(final LocalPlayer player) {
		final Input input = player.input;
		
		if (MINECRAFT.screen == null && !player.getAbilities().flying) {
			input.jumping |= toggleJump.tick();
		}
		
		if (FlightHelper.isFlyingCreativeOrSpectator(player)) {
			final boolean boost = KEY_SPRINT.isDown();
			final float flightSpeed = FlightHelper.getFlightSpeed(player, boost);
			final float verticalVelocity = FlightHelper.getExtraVerticalVelocity(player, boost);
			
			if (flightSpeed > 0F) {
				player.getAbilities().setFlyingSpeed(flightSpeed);
			}
			
			if (Math.abs(verticalVelocity) > 1E-5F && player == MINECRAFT.getCameraEntity()) {
				int direction = 0;
				
				if (input.shiftKeyDown) {
					--direction;
				}
				
				if (input.jumping) {
					++direction;
				}
				
				if (direction != 0) {
					player.setDeltaMovement(player.getDeltaMovement().add(0D, flightSpeed * verticalVelocity * direction, 0D));
				}
			}
		}
		
		if (cfg().resumeSprintingAfterHittingObstacle) {
			if (wasHittingObstacle != player.horizontalCollision) {
				if (!wasHittingObstacle) {
					wasSprintingBeforeHittingObstacle = player.isSprinting() || KEY_SPRINT.isDown();
				}
				else if (wasSprintingBeforeHittingObstacle) {
					wasSprintingBeforeHittingObstacle = false;
					temporarySprintTimer = 10;
				}
				
				// collision also stops when the player lets go of movement keys
				wasHittingObstacle = player.horizontalCollision;
			}
		}
		else {
			wasHittingObstacle = player.horizontalCollision;
			wasSprintingBeforeHittingObstacle = false;
		}
	}
	
	public void afterSuperCall(final LocalPlayer player) {
		if (FlightHelper.shouldFlyOnGround(player)) {
			final boolean isSneaking = player.isShiftKeyDown();
			final boolean isOnGround = player.isOnGround();
			
			if (!isSneaking) {
				wasSneakingBeforeTouchingGround = false;
			}
			else if (!isOnGround) {
				wasSneakingBeforeTouchingGround = true;
			}
			
			if (!isOnGround) {
				holdingSneakWhileTouchingGround = false;
			}
			else {
				boolean cancelLanding = true;
				
				if (!wasSneakingBeforeTouchingGround) {
					if (isSneaking) {
						holdingSneakWhileTouchingGround = true;
					}
					else if (holdingSneakWhileTouchingGround) {
						player.getAbilities().flying = false;
						player.onUpdateAbilities();
						cancelLanding = false;
					}
				}
				
				if (cancelLanding) {
					player.setOnGround(false);
				}
			}
		}
		else {
			wasSneakingBeforeTouchingGround = false;
			holdingSneakWhileTouchingGround = false;
		}
		
		if (FlightHelper.isFlyingCreativeOrSpectator(player) && cfg().disableFlightInertia) {
			final Input input = player.input;
			
			if (input.forwardImpulse == 0F && input.leftImpulse == 0F) {
				player.setDeltaMovement(player.getDeltaMovement().multiply(0.0, 1.0, 0.0));
			}
			
			if (!input.jumping && !input.shiftKeyDown) {
				player.setDeltaMovement(player.getDeltaMovement().multiply(1.0, 0.0, 1.0));
			}
		}
		
		if (player.isCreative()) {
			if (cfg().keyToggleFlight.consumeClick()) {
				final boolean isFlying = !player.getAbilities().flying;
				
				player.getAbilities().flying = isFlying;
				player.onUpdateAbilities();
				
				if (isFlying) {
					temporaryFlyOnGroundTimer = 10;
				}
			}
			
			if (temporaryFlyOnGroundTimer > 0) {
				if (player.isShiftKeyDown()) {
					temporaryFlyOnGroundTimer = 0;
				}
				else {
					--temporaryFlyOnGroundTimer;
					player.setOnGround(false);
				}
			}
		}
		else {
			temporaryFlyOnGroundTimer = 0;
		}
		
		if (!cfg().sneakingMovesCameraSmoothly) {
			final Camera camera = MINECRAFT.gameRenderer.getMainCamera();
			
			if (camera.getEntity() == player) {
				((AccessCameraFields)camera).setEyeHeight(player.getEyeHeight());
			}
		}
		
		if (cfg().keyResetAllToggles.consumeClick()) {
			toggleSprint.reset();
			toggleSneak.reset();
			toggleWalkForward.reset();
			toggleJump.reset();
		}
		
		if (cfg().keyOpenMenu.isDown()) {
			MINECRAFT.setScreen(new BetterControlsScreen(null));
		}
	}
	
	public boolean shouldResetFOV(final LocalPlayer player) {
		return cfg().disableChangingFovWhileFlying && FlightHelper.isFlyingCreativeOrSpectator(player);
	}
}
