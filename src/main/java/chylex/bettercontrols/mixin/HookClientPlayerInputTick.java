package chylex.bettercontrols.mixin;

import chylex.bettercontrols.player.PlayerTicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(KeyboardInput.class)
@SuppressWarnings("UnreachableCode")
public abstract class HookClientPlayerInputTick {
	@Inject(
		method = "tick",
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/KeyboardInput;keyPresses:Lnet/minecraft/world/entity/player/Input;", ordinal = 0, shift = AFTER)
	)
	private void afterInputTick(final CallbackInfo info) {
		final LocalPlayer player = Minecraft.getInstance().player;
		
		if (player != null) {
			PlayerTicker.get(player).afterKeyboardInputAssigned(player);
		}
	}
}
