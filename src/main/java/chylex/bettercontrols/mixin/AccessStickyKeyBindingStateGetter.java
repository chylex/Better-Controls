package chylex.bettercontrols.mixin;
import net.minecraft.client.settings.ToggleableKeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.function.BooleanSupplier;

@Mixin(ToggleableKeyBinding.class)
public interface AccessStickyKeyBindingStateGetter{
	@Accessor("getterToggle")
	BooleanSupplier getToggleGetter();
	
	@Accessor("getterToggle")
	@Mutable
	void setToggleGetter(final BooleanSupplier toggleGetter);
}
