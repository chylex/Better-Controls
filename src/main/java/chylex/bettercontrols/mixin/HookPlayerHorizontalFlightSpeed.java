package chylex.bettercontrols.mixin;

import chylex.bettercontrols.Mixins;
import chylex.bettercontrols.player.FlightHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Player.class)
public abstract class HookPlayerHorizontalFlightSpeed extends LivingEntity {
	protected HookPlayerHorizontalFlightSpeed(EntityType<? extends LivingEntity> type, Level world) {
		super(type, world);
	}
	
	@SuppressWarnings("SimplifiableIfStatement")
	@ModifyExpressionValue(
		method = "getFlyingSpeed",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z"),
		slice = @Slice(
			from = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;flying:Z"),
			to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Abilities;getFlyingSpeed()F")
		)
	)
	private boolean disableVanillaSprintBoost(boolean isSprinting) {
		Player me = Mixins.me(this);
		
		if (me instanceof LocalPlayer localPlayer && FlightHelper.isFlyingCreativeOrSpectator(localPlayer)) {
			return false;
		}
		else {
			return isSprinting;
		}
	}
	
	@ModifyReturnValue(method = "getFlyingSpeed", at = @At("RETURN"))
	private float modifyHorizontalFlyingSpeed(float flyingSpeed) {
		Player me = Mixins.me(this);
		
		if (me instanceof LocalPlayer localPlayer && localPlayer.getAbilities().flying) {
			return flyingSpeed * FlightHelper.getHorizontalSpeedMultiplier(localPlayer);
		}
		else {
			return flyingSpeed;
		}
	}
}
