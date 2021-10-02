package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.screens.controls.ControlList.CategoryEntry;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CategoryEntry.class)
public interface AccessControlsListCategory {
	@Accessor("name")
	Component getText();
}
