package chylex.bettercontrols.mixin;
import chylex.bettercontrols.gui.ScreenPatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

@Mixin(value = Minecraft.class, priority = 100)
public abstract class HookOpenScreen {
	@Inject(method = "setScreen", at = @At("TAIL"))
	private void openScreen(final Screen ignore, final CallbackInfo ci) {
		final Screen screen = MINECRAFT.screen;
		
		if (screen != null && !Screen.hasAltDown()) {
			if (screen.getClass() == ControlsScreen.class) {
				ScreenPatcher.onControlsScreenOpened((ControlsScreen)screen);
			}
			else if (screen.getClass() == AccessibilityOptionsScreen.class) {
				ScreenPatcher.onAccessibilityScreenOpened((AccessibilityOptionsScreen)screen);
			}
		}
	}
}
