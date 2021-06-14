package chylex.bettercontrols.mixin;
import chylex.bettercontrols.gui.ScreenPatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

@Mixin(value = MinecraftClient.class, priority = 100)
public abstract class HookOpenScreen {
	@Inject(method = "openScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("TAIL"))
	private void openScreen(final Screen ignore, final CallbackInfo ci) {
		final Screen screen = MINECRAFT.currentScreen;
		
		if (screen != null && !Screen.hasAltDown()) {
			if (screen.getClass() == ControlsOptionsScreen.class) {
				ScreenPatcher.onControlsScreenOpened((ControlsOptionsScreen)screen);
			}
			else if (screen.getClass() == AccessibilityOptionsScreen.class) {
				ScreenPatcher.onAccessibilityScreenOpened((AccessibilityOptionsScreen)screen);
			}
		}
	}
}
