package chylex.bettercontrols.mixin;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.Map;

@Mixin(KeyBinding.class)
public interface AccessKeyBindingFields{
	@Accessor
	static Map<String, Integer> getCategoryOrderMap(){
		throw new AssertionError();
	}
	
	@Accessor("pressed")
	boolean isPressedField();
	
	@Accessor("pressed")
	void setPressedField(boolean value);
}
