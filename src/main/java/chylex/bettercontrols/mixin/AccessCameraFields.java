package chylex.bettercontrols.mixin;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface AccessCameraFields{
	@Accessor
	Entity getFocusedEntity();
	
	@Accessor
	void setCameraY(float y);
}
