package chylex.bettercontrols.mixin;

import chylex.bettercontrols.player.FlightHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LocalPlayer.class)
@SuppressWarnings("MethodMayBeStatic")
public abstract class HookPlayerGliding {
	@ModifyExpressionValue(
		method = "aiStep",
		at = @At(value = "INVOKE:LAST", target = "Lnet/minecraft/world/entity/player/Input;jump()Z"),
		slice = @Slice(
			to = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;tryToStartFallFlying()Z")
		)
	)
	private boolean shouldStartGliding(boolean isHoldingJump) {
		return FlightHelper.shouldStartGliding(isHoldingJump);
	}
}
