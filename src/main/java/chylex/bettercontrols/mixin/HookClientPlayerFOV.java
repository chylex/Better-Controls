package chylex.bettercontrols.mixin;

import chylex.bettercontrols.player.PlayerTicker;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

@Mixin(AbstractClientPlayer.class)
public abstract class HookClientPlayerFOV {
	@Redirect(
		method = "getFieldOfViewModifier",
		at = @At(value = "INVOKE", target = "Ljava/lang/Float;isNaN(F)Z"),
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Abilities;getWalkingSpeed()F"),
			to = @At(value = "INVOKE", target = "Ljava/lang/Float;isInfinite(F)Z")
		)
	)
	private boolean resetFOV(float movementSpeed) {
		final LocalPlayer player = MINECRAFT.player;
		return (player != null && PlayerTicker.get(player).shouldResetFOV(player)) || Float.isNaN(movementSpeed);
	}
}
