package chylex.bettercontrols.mixin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@SuppressWarnings("MethodMayBeStatic")
@Mixin(Player.class)
public abstract class HookPlayerFlightSpeed extends LivingEntity {
	protected HookPlayerFlightSpeed(final EntityType<? extends LivingEntity> type, final Level world) {
		super(type, world);
	}
	
	@Redirect(
		method = "travel",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z"),
		slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Abilities;getFlyingSpeed()F"))
	)
	private boolean disableVanillaSprintBoost(final Player player) {
		return false;
	}
}
