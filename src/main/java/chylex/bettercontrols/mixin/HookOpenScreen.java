package chylex.bettercontrols.mixin;
import chylex.bettercontrols.gui.ControlsScreenPatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class HookOpenScreen{
	@Inject(method = "openScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("TAIL"))
	private void openScreen(final Screen ignore, final CallbackInfo ci){
		final MinecraftClient mc = MinecraftClient.getInstance();
		final Screen screen = mc.currentScreen;
		
		if (screen != null && screen.getClass() == ControlsOptionsScreen.class && !Screen.hasAltDown()){
			ControlsScreenPatcher.onOpened((ControlsOptionsScreen)screen);
		}
	}
}
