package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.widget.list.KeyBindingList.KeyEntry;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyEntry.class)
public interface AccessControlsListKeyBinding{
	@Accessor("keybinding")
	KeyBinding getBinding();
}
