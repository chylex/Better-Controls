package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.screen.options.ControlsListWidget.CategoryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CategoryEntry.class)
public interface AccessControlsListCategory{
	@Accessor
	String getName();
}
