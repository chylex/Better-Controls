package chylex.bettercontrols.mixin;

import chylex.bettercontrols.gui.BetterControlsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(ControlsScreen.class)
@SuppressWarnings("UnreachableCode")
public abstract class HookControlsScreen extends OptionsSubScreen {
	public HookControlsScreen(final Screen parentScreen, final Options options, final Component title) {
		super(parentScreen, options, title);
	}
	
	@Inject(method = "addOptions", at = @At("RETURN"))
	public void afterAddOptions(final CallbackInfo ci) {
		@SuppressWarnings("ConstantConditions")
		final ControlsScreen screen = (ControlsScreen)(Object)this;
		
		if (list != null) {
			final MutableComponent buttonTitle = BetterControlsScreen.TITLE.plainCopy().append("...");
			list.addSmall(List.of(Button.builder(buttonTitle, btn -> showOptionsScreen(screen)).build()));
		}
	}
	
	@Unique
	private static void showOptionsScreen(final ControlsScreen screen) {
		final Minecraft mc = Minecraft.getInstance();
		mc.setScreen(new BetterControlsScreen(mc, screen));
	}
}
