package chylex.bettercontrols.mixin;

import chylex.bettercontrols.BetterControlsCommon;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsList.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KeyBindsList.class)
public abstract class HookControlsListWidget extends ContainerObjectSelectionList<Entry> {
	public HookControlsListWidget(final Minecraft client, final int width, final int height, final int top, final int itemHeight) {
		super(client, width, height, top, itemHeight);
	}
	
	@Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;keyMappings:[Lnet/minecraft/client/KeyMapping;"))
	private KeyMapping[] excludeOwnKeyBindings(final Options options) {
		return ArrayUtils.removeElements(options.keyMappings, BetterControlsCommon.getConfig().getAllKeyBindings());
	}
}
