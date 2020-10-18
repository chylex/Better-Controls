package chylex.bettercontrols.mixin;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.options.ControlsListWidget;
import net.minecraft.client.gui.screen.options.ControlsListWidget.CategoryEntry;
import net.minecraft.client.gui.screen.options.ControlsListWidget.Entry;
import net.minecraft.client.gui.screen.options.ControlsListWidget.KeyBindingEntry;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsListWidget.class)
public abstract class HookControlsListWidget extends ElementListWidget<Entry>{
	public HookControlsListWidget(final MinecraftClient client, final int width, final int height, final int top, final int bottom, final int itemHeight){
		super(client, width, height, top, bottom, itemHeight);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(final ControlsOptionsScreen parent, final MinecraftClient client, final CallbackInfo ci){
		children().removeIf(it -> {
			if (it instanceof CategoryEntry && KeyBindingWithModifier.checkCategoryMatches(((AccessControlsListCategory)it).getName())){
				return true;
			}
			
			if (it instanceof KeyBindingEntry && ArrayUtils.contains(BetterControlsMod.config.getAllKeyBindings(), ((AccessControlsListKeyBinding)it).getBinding())){
				return true;
			}
			
			return false;
		});
	}
}
