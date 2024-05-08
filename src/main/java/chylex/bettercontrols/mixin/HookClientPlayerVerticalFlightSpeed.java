package chylex.bettercontrols.mixin;

import chylex.bettercontrols.player.FlightHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LocalPlayer.class)
@SuppressWarnings({ "SameReturnValue", "UnreachableCode" })
public abstract class HookClientPlayerVerticalFlightSpeed extends LivingEntity {
	protected HookClientPlayerVerticalFlightSpeed(final EntityType<? extends LivingEntity> type, final Level world) {
		super(type, world);
	}
	
	@Redirect(
		method = "aiStep",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Abilities;getFlyingSpeed()F"),
		slice = @Slice(
			from = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;flying:Z"),
			to = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V")
		)
	)
	private float modifyVerticalFlightSpeed(final Abilities abilities) {
		@SuppressWarnings("ConstantConditions")
		final LocalPlayer me = (LocalPlayer)(Object)this;
		return abilities.getFlyingSpeed() * FlightHelper.getVerticalSpeedMultiplier(me);
	}
}
