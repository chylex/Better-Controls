package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.settings.AbstractOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OptionButton.class)
public interface AccessOptionButtonWidgetOption{
	@Accessor("enumOptions")
	AbstractOption getOption();
}
