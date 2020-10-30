package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.List;

@Mixin(Screen.class)
public interface AccessScreenButtons{
	@Accessor
	List<AbstractButton> getButtons();
	
	@Invoker
	<T extends Widget> T callAddButton(T button);
}
