package chylex.bettercontrols.mixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PlayerEntity.class)
public abstract class HookPlayerFlightSpeed extends LivingEntity {
	protected HookPlayerFlightSpeed(final EntityType<? extends LivingEntity> type, final World world) {
		super(type, world);
	}
	
	@Redirect(
		method = "travel",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSprinting()Z"),
		slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerAbilities;getFlySpeed()F"))
	)
	private boolean disableVanillaSprintBoost(final PlayerEntity player) {
		return false;
	}
}
