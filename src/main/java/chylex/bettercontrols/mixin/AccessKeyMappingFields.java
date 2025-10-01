package chylex.bettercontrols.mixin;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public interface AccessKeyMappingFields {
	@Accessor("isDown")
	boolean isPressedField();
	
	@Accessor("isDown")
	void setPressedField(boolean value);
}
