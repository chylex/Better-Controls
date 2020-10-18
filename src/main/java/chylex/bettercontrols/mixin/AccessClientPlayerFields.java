package chylex.bettercontrols.mixin;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerEntity.class)
public interface AccessClientPlayerFields{
	@Accessor("field_3935")
	void setTicksLeftToDoubleTapSprint(int value);
}
