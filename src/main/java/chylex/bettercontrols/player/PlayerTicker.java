package chylex.bettercontrols.player;

import chylex.bettercontrols.Mixins;
import chylex.bettercontrols.gui.BetterControlsScreen;
import chylex.bettercontrols.input.SprintMode;
import chylex.bettercontrols.input.ToggleTracker;
import chylex.bettercontrols.input.ToggleTrackerForStickyKey;
import chylex.bettercontrols.mixin.AccessToggleKeyMappingFields;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import java.lang.ref.WeakReference;
import java.util.function.BooleanSupplier;
import static chylex.bettercontrols.BetterControlsCommon.getConfig;

public final class PlayerTicker {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();
	private static final Options OPTIONS = MINECRAFT.options;
	
	private static final ToggleKeyMapping KEY_SPRINT = (ToggleKeyMapping) OPTIONS.keySprint;
	private static final KeyMapping KEY_SNEAK = OPTIONS.keyShift;
	private static final KeyMapping KEY_FORWARD = OPTIONS.keyUp;
	private static final KeyMapping KEY_JUMP = OPTIONS.keyJump;
	
	private static PlayerTicker ticker = new PlayerTicker(null);
	
	public static PlayerTicker get(LocalPlayer player) {
		if (ticker.ref.get() != player) {
			ticker = new PlayerTicker(player);
		}
		
		return ticker;
	}
	
	private final WeakReference<LocalPlayer> ref;
	
	private PlayerTicker(LocalPlayer player) {
		this.ref = new WeakReference<>(player);
		setup();
	}
	
	// Logic
	
	private final ToggleTracker toggleSprint = new ToggleTrackerForStickyKey(getConfig().keyToggleSprint, KEY_SPRINT, OPTIONS.toggleSprint());
	private final ToggleTracker toggleSneak = new ToggleTrackerForStickyKey(getConfig().keyToggleSneak, KEY_SNEAK, OPTIONS.toggleCrouch());
	private final ToggleTracker toggleWalkForward = new ToggleTracker(getConfig().keyToggleWalkForward, KEY_FORWARD);
	private final ToggleTracker toggleJump = new ToggleTracker(getConfig().keyToggleJump, KEY_JUMP);
	
	private boolean waitingForSprintKeyRelease = false;
	private boolean stopSprintingAfterReleasingSprintKey = false;
	
	private boolean wasHittingObstacle = false;
	private boolean wasSprintingBeforeHittingObstacle = false;
	private int temporarySprintTimer = 0;
	
	private boolean wasSneakingBeforeTouchingGround = false;
	private boolean holdingSneakWhileTouchingGround = false;
	private int temporaryFlyOnGroundTimer = 0;
	
	private void setup() {
		AccessToggleKeyMappingFields sprint = Mixins.toggleKeyMappingFields(KEY_SPRINT);
		BooleanSupplier getter = sprint.getNeedsToggle();
		
		if (getter instanceof SprintPressGetter g) {
			getter = g.wrapped();
		}
		
		sprint.setNeedsToggle(new SprintPressGetter(getter, () -> temporarySprintTimer > 0));
	}
	
	public void atHead(LocalPlayer player) {
		if (FlightHelper.shouldFlyOnGround(player)) {
			player.setOnGround(false);
		}
		
		if (!getConfig().doubleTapForwardToSprint) {
			Mixins.clientPlayerFields(player).setSprintTriggerTime(0);
		}
		
		if (!getConfig().doubleTapJumpToToggleFlight) {
			Mixins.playerFields(player).setJumpTriggerTime(0);
		}
		
		SprintMode sprintMode = getConfig().sprintMode;
		boolean wasSprintToggled = Boolean.TRUE.equals(OPTIONS.toggleSprint().get());
		boolean isSprintToggled = toggleSprint.tick();
		
		if (temporarySprintTimer > 0) {
			stopSprintingAfterReleasingSprintKey = false;
			waitingForSprintKeyRelease = false;
			
			int nextTemporarySprintTimer = temporarySprintTimer - 1;
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
	
	public void afterKeyboardInputAssigned(LocalPlayer player) {
		if (MINECRAFT.screen == null && toggleWalkForward.tick()) {
			ClientInput input = player.input;
			
			input.keyPresses = new Input(
				true,
				input.keyPresses.backward(),
				input.keyPresses.left(),
				input.keyPresses.right(),
				input.keyPresses.jump(),
				input.keyPresses.shift(),
				input.keyPresses.sprint()
			);
		}
	}
	
	public void afterInputTick(LocalPlayer player) {
		if (MINECRAFT.screen == null && !player.getAbilities().flying && toggleJump.tick()) {
			player.input.makeJump();
		}
		
		if (getConfig().resumeSprintingAfterHittingObstacle) {
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
	
	public void afterSuperCall(LocalPlayer player) {
		if (FlightHelper.shouldFlyOnGround(player)) {
			boolean isSneaking = player.isShiftKeyDown();
			boolean isOnGround = player.onGround();
			
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
		
		if (FlightHelper.isFlyingCreativeOrSpectator(player) && getConfig().disableFlightInertia) {
			ClientInput input = player.input;
			
			if (input.forwardImpulse == 0F && input.leftImpulse == 0F) {
				player.setDeltaMovement(player.getDeltaMovement().multiply(0.0, 1.0, 0.0));
			}
			
			if (!input.keyPresses.jump() && !input.keyPresses.shift()) {
				player.setDeltaMovement(player.getDeltaMovement().multiply(1.0, 0.0, 1.0));
			}
		}
		
		if (player.isCreative()) {
			if (getConfig().keyToggleFlight.consumeClick()) {
				boolean isFlying = !player.getAbilities().flying;
				
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
		
		if (!getConfig().sneakingMovesCameraSmoothly) {
			Camera camera = MINECRAFT.gameRenderer.getMainCamera();
			
			if (camera.getEntity() == player) {
				Mixins.cameraFields(camera).setEyeHeight(player.getEyeHeight());
			}
		}
		
		if (getConfig().keyResetAllToggles.consumeClick()) {
			toggleSprint.reset();
			toggleSneak.reset();
			toggleWalkForward.reset();
			toggleJump.reset();
		}
		
		if (getConfig().keyOpenMenu.isDown()) {
			MINECRAFT.setScreen(new BetterControlsScreen(null));
		}
	}
	
	public static boolean shouldResetFOV(LocalPlayer player) {
		return getConfig().disableChangingFovWhileFlying && FlightHelper.isFlyingCreativeOrSpectator(player);
	}
}
