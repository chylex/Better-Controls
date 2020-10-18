package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OptionButtonWidget.class)
public interface AccessOptionButtonWidgetOption{
	@Accessor
	Option getOption();
}
