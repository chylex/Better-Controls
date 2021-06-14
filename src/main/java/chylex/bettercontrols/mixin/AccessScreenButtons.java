package chylex.bettercontrols.mixin;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.List;

@Mixin(Screen.class)
public interface AccessScreenButtons {
	@Accessor
	List<Element> getChildren();
	
	@Accessor
	List<Selectable> getSelectables();
	
	@Accessor
	List<Drawable> getDrawables();
	
	@Invoker
	void callRemove(Element child);
}
