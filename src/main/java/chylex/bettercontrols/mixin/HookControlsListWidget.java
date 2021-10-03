package chylex.bettercontrols.mixin;
import chylex.bettercontrols.BetterControlsCommon;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.controls.ControlList;
import net.minecraft.client.gui.screens.controls.ControlList.CategoryEntry;
import net.minecraft.client.gui.screens.controls.ControlList.Entry;
import net.minecraft.client.gui.screens.controls.ControlList.KeyEntry;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlList.class)
public abstract class HookControlsListWidget extends ContainerObjectSelectionList<Entry> {
	public HookControlsListWidget(final Minecraft client, final int width, final int height, final int top, final int bottom, final int itemHeight) {
		super(client, width, height, top, bottom, itemHeight);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(final ControlsScreen parent, final Minecraft client, final CallbackInfo ci) {
		children().removeIf(it -> {
			if (it instanceof final CategoryEntry entry && KeyBindingWithModifier.checkCategoryMatches(((AccessControlsListCategory)entry).getName())) {
				return true;
			}
			
			if (it instanceof final KeyEntry entry && ArrayUtils.contains(BetterControlsCommon.getConfig().getAllKeyBindings(), ((AccessControlsListKeyBinding)entry).getKey())) {
				return true;
			}
			
			return false;
		});
	}
}
