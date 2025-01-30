package chylex.bettercontrols.mixin;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.Map;

@Mixin(KeyMapping.class)
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public interface AccessKeyMappingFields {
	@Accessor("CATEGORY_SORT_ORDER")
	static Map<String, Integer> getCategoryOrderMap() {
		throw new AssertionError();
	}
	
	@Accessor("isDown")
	boolean isPressedField();
	
	@Accessor("isDown")
	void setPressedField(boolean value);
}
