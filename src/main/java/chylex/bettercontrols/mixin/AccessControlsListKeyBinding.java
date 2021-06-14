package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.screen.option.ControlsListWidget.KeyBindingEntry;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBindingEntry.class)
public interface AccessControlsListKeyBinding {
	@Accessor
	KeyBinding getBinding();
}
