package chylex.bettercontrols.mixin;
import chylex.bettercontrols.player.PlayerTicker;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static chylex.bettercontrols.util.Statics.MINECRAFT;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(KeyboardInput.class)
public abstract class HookClientPlayerInputTick {
	@Inject(method = "tick(Z)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/KeyboardInput;up:Z", ordinal = 0, shift = AFTER))
	private void afterInputTick(final CallbackInfo info) {
		final Input input = (Input)(Object)this;
		final LocalPlayer player = MINECRAFT.player;
		
		if (player != null) {
			PlayerTicker.get(player).afterInputAssignsPressingForward(input);
		}
	}
}
