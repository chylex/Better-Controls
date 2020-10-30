package chylex.bettercontrols.mixin;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ActiveRenderInfo.class)
public interface AccessCameraFields{
	@Accessor("renderViewEntity")
	Entity getFocusedEntity();
	
	@Accessor("height")
	void setCameraY(float y);
}
