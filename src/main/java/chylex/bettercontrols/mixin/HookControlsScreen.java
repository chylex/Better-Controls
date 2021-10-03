package chylex.bettercontrols.mixin;

import chylex.bettercontrols.gui.BetterControlsScreen;
import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ControlsScreen.class)
public abstract class HookControlsScreen {
	@Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CycleOption;createButton(Lnet/minecraft/client/Options;III)Lnet/minecraft/client/gui/components/AbstractWidget;"))
	private AbstractWidget replaceAutoJumpButton(final CycleOption<?> option, final Options options, final int x, final int y, final int width) {
		if (option == Option.AUTO_JUMP && !Screen.hasAltDown()) {
			@SuppressWarnings("ConstantConditions")
			final ControlsScreen screen = (ControlsScreen)(Object)this;
			return new Button(x, y, width, 20, BetterControlsScreen.TITLE.plainCopy().append("..."), btn -> Minecraft.getInstance().setScreen(new BetterControlsScreen(screen)));
		}
		
		return option.createButton(options, x, y, width);
	}
}
