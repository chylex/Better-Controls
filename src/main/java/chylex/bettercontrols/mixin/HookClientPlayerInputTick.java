package chylex.bettercontrols.mixin;
import chylex.bettercontrols.player.PlayerTicker;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static chylex.bettercontrols.util.Statics.MINECRAFT;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(MovementInputFromOptions.class)
public abstract class HookClientPlayerInputTick{
	@Inject(method = "tickMovement(Z)V", at = @At(value = "FIELD", target = "Lnet/minecraft/util/MovementInputFromOptions;forwardKeyDown:Z", ordinal = 0, shift = AFTER))
	private void afterInputTick(final CallbackInfo info){
		final MovementInput input = (MovementInput)(Object)this;
		final ClientPlayerEntity player = MINECRAFT.player;
		
		if (player != null){
			PlayerTicker.get(player).afterInputAssignsPressingForward(input);
		}
	}
}
