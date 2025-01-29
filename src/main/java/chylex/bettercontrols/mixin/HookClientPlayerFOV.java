package chylex.bettercontrols.mixin;

import chylex.bettercontrols.player.PlayerTicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayer.class)
public abstract class HookClientPlayerFOV {
	@Redirect(
		method = "getFieldOfViewModifier",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Abilities;getWalkingSpeed()F")
	)
	private float overrideWalkingSpeed(final Abilities abilities) {
		final LocalPlayer player = Minecraft.getInstance().player;
		return (player != null && PlayerTicker.get(player).shouldResetFOV(player)) ? 0F : abilities.getWalkingSpeed();
	}
}
