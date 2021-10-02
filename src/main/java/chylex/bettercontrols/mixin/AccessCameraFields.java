package chylex.bettercontrols.mixin;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface AccessCameraFields {
	@Accessor("eyeHeight")
	void setCameraY(float y);
}
