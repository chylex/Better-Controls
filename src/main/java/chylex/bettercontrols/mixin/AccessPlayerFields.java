package chylex.bettercontrols.mixin;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface AccessPlayerFields{
	@Accessor("jumpTriggerTime")
	void setTicksLeftToDoubleTapFlight(int value);
}
