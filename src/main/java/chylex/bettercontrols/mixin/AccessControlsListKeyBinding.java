package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.screen.options.ControlsListWidget.KeyBindingEntry;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBindingEntry.class)
public interface AccessControlsListKeyBinding{
	@Accessor
	KeyBinding getBinding();
}
