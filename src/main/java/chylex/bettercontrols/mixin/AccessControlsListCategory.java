package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.widget.list.KeyBindingList.CategoryEntry;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CategoryEntry.class)
public interface AccessControlsListCategory{
	@Accessor("labelText")
	ITextComponent getText();
}
