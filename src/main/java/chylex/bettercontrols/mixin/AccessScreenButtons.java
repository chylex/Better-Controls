package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface AccessScreenButtons {
	@Invoker
	<T extends GuiEventListener & Widget & NarratableEntry> T callAddRenderableWidget(T widget);
	
	@Invoker
	void callRemoveWidget(GuiEventListener widget);
}
