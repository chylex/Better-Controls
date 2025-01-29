package chylex.bettercontrols.mixin;

import chylex.bettercontrols.player.PlayerTicker;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public abstract class HookClientPlayerFOV {
	@ModifyExpressionValue(
		method = "getFieldOfViewModifier",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Abilities;getWalkingSpeed()F")
	)
	private float overrideWalkingSpeed(float walkingSpeed) {
		AbstractClientPlayer me = (AbstractClientPlayer)(Object)this;
		
		if (me instanceof LocalPlayer localPlayer && PlayerTicker.get(localPlayer).shouldResetFOV(localPlayer)) {
			return 0F;
		}
		else {
			return walkingSpeed;
		}
	}
}
