package chylex.bettercontrols.mixin;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.controls.ControlList.KeyEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyEntry.class)
public interface AccessControlsListKeyBinding {
	@Accessor("key")
	KeyMapping getBinding();
}
