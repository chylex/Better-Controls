package chylex.bettercontrols.mixin;
import net.minecraft.client.option.StickyKeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.function.BooleanSupplier;

@Mixin(StickyKeyBinding.class)
public interface AccessStickyKeyBindingStateGetter {
	@Accessor
	BooleanSupplier getToggleGetter();
	
	@Accessor
	@Mutable
	void setToggleGetter(final BooleanSupplier toggleGetter);
}
