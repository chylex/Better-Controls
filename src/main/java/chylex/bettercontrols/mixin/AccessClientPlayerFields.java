package chylex.bettercontrols.mixin;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerEntity.class)
public interface AccessClientPlayerFields{
	@Accessor("sprintToggleTimer")
	void setTicksLeftToDoubleTapSprint(int value);
}
