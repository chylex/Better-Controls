package chylex.bettercontrols.mixin;

import chylex.bettercontrols.player.PlayerTicker;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(LocalPlayer.class)
@SuppressWarnings("UnreachableCode")
public abstract class HookClientPlayerTick extends AbstractClientPlayer {
	protected HookClientPlayerTick(final ClientLevel world, final GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(method = "aiStep()V", at = @At("HEAD"))
	private void atHead(final CallbackInfo info) {
		@SuppressWarnings("ConstantConditions")
		final LocalPlayer player = (LocalPlayer)(Object)this;
		PlayerTicker.get(player).atHead(player);
	}
	
	@Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", ordinal = 0, shift = AFTER))
	private void afterInputTick(final CallbackInfo info) {
		@SuppressWarnings("ConstantConditions")
		final LocalPlayer player = (LocalPlayer)(Object)this;
		PlayerTicker.get(player).afterInputTick(player);
	}
	
	@Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;aiStep()V", ordinal = 0, shift = AFTER))
	private void afterSuperCall(final CallbackInfo info) {
		@SuppressWarnings("ConstantConditions")
		final LocalPlayer player = (LocalPlayer)(Object)this;
		PlayerTicker.get(player).afterSuperCall(player);
	}
}
