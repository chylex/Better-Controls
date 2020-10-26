package chylex.bettercontrols.mixin;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface AccessGameRendererFields{
	@Accessor
	void setMovementFovMultiplier(float value);
	
	@Accessor
	void setLastMovementFovMultiplier(float value);
}
