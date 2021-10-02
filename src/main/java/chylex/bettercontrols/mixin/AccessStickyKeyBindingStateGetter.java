package chylex.bettercontrols.mixin;
import net.minecraft.client.ToggleKeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.function.BooleanSupplier;

@Mixin(ToggleKeyMapping.class)
public interface AccessStickyKeyBindingStateGetter{
	@Accessor("needsToggle")
	BooleanSupplier getToggleGetter();
	
	@Accessor("needsToggle")
	@Mutable
	void setToggleGetter(final BooleanSupplier toggleGetter);
}
