package chylex.bettercontrols.mixin;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface AccessGameRendererFields{
	@Accessor("fovModifierHand")
	void setMovementFovMultiplier(float value);
}
