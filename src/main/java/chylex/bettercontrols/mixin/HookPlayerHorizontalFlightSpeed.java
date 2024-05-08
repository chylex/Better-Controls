package chylex.bettercontrols.mixin;

import chylex.bettercontrols.player.FlightHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
@SuppressWarnings({ "SameReturnValue", "UnreachableCode" })
public abstract class HookPlayerHorizontalFlightSpeed extends LivingEntity {
	protected HookPlayerHorizontalFlightSpeed(final EntityType<? extends LivingEntity> type, final Level world) {
		super(type, world);
	}
	
	@SuppressWarnings("SimplifiableIfStatement")
	@Redirect(
		method = "getFlyingSpeed",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z"),
		slice = @Slice(
			from = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;flying:Z"),
			to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Abilities;getFlyingSpeed()F")
		)
	)
	private boolean disableVanillaSprintBoost(final Player player) {
		if (player instanceof LocalPlayer) {
			return false;
		}
		else {
			return player.isSprinting();
		}
	}
	
	@Inject(method = "getFlyingSpeed", at = @At("RETURN"), cancellable = true)
	private void modifyHorizontalFlyingSpeed(final CallbackInfoReturnable<Float> cir) {
		@SuppressWarnings("ConstantConditions")
		final Player me = (Player)(Object)this;
		
		if (me instanceof final LocalPlayer localPlayer && localPlayer.getAbilities().flying) {
			final float multiplier = FlightHelper.getHorizontalSpeedMultiplier(localPlayer);
			cir.setReturnValue(Float.valueOf(cir.getReturnValueF() * multiplier));
		}
	}
}
