package chylex.bettercontrols.mixin;
import chylex.bettercontrols.gui.ScreenPatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

@Mixin(value = Minecraft.class, priority = 100)
public abstract class HookOpenScreen{
	@Inject(method = "displayGuiScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("TAIL"))
	private void openScreen(final Screen ignore, final CallbackInfo ci){
		final Screen screen = MINECRAFT.currentScreen;
		
		if (screen != null && !Screen.hasAltDown()){
			if (screen.getClass() == ControlsScreen.class){
				ScreenPatcher.onControlsScreenOpened((ControlsScreen)screen);
			}
			else if (screen.getClass() == AccessibilityScreen.class){
				ScreenPatcher.onAccessibilityScreenOpened((AccessibilityScreen)screen);
			}
		}
	}
}
