package chylex.bettercontrols.mixin;
import chylex.bettercontrols.player.PlayerTicker;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static chylex.bettercontrols.util.Statics.MINECRAFT;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(KeyboardInput.class)
public abstract class HookClientPlayerInputTick {
	@Inject(method = "tick(Z)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/KeyboardInput;pressingForward:Z", ordinal = 0, shift = AFTER))
	private void afterInputTick(final CallbackInfo info) {
		final KeyboardInput input = (KeyboardInput)(Object)this;
		final ClientPlayerEntity player = MINECRAFT.player;
		
		if (player != null) {
			PlayerTicker.get(player).afterInputAssignsPressingForward(input);
		}
	}
}
