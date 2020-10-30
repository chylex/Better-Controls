package chylex.bettercontrols.mixin;
import chylex.bettercontrols.player.PlayerTicker;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(ClientPlayerEntity.class)
public abstract class HookClientPlayerTick extends AbstractClientPlayerEntity{
	protected HookClientPlayerTick(final ClientWorld world, final GameProfile profile){
		super(world, profile);
	}
	
	@Inject(method = "livingTick", at = @At("HEAD"))
	private void atHead(final CallbackInfo info){
		final ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
		PlayerTicker.get(player).atHead(player);
	}
	
	@Inject(method = "livingTick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovementInput;tickMovement(Z)V", ordinal = 0, shift = AFTER))
	private void afterInputTick(final CallbackInfo info){
		final ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
		PlayerTicker.get(player).afterInputTick(player);
	}
	
	@Inject(method = "livingTick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;livingTick()V", ordinal = 0, shift = AFTER))
	private void afterSuperCall(final CallbackInfo info){
		final ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
		PlayerTicker.get(player).afterSuperCall(player);
	}
}
