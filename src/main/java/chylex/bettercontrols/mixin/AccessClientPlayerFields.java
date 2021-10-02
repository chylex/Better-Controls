package chylex.bettercontrols.mixin;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface AccessClientPlayerFields{
	@Accessor("sprintTriggerTime")
	void setTicksLeftToDoubleTapSprint(int value);
}
