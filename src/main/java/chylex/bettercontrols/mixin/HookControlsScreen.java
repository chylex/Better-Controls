package chylex.bettercontrols.mixin;

import chylex.bettercontrols.gui.BetterControlsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public abstract class HookControlsScreen extends OptionsSubScreen {
	public HookControlsScreen(final Screen parentScreen, final Options options, final Component title) {
		super(parentScreen, options, title);
	}
	
	@Inject(method = "init", at = @At("RETURN"))
	public void afterInit(final CallbackInfo ci) {
		@SuppressWarnings("ConstantConditions")
		final ControlsScreen screen = (ControlsScreen)(Object)this;
		final int center = width / 2;
		
		int leftBottomY = 0;
		int rightBottomY = 0;
		AbstractWidget doneButton = null;
		
		for (final GuiEventListener child : children()) {
			if (!(child instanceof final AbstractWidget widget)) {
				continue;
			}
			
			if (widget instanceof final Button button && button.getMessage() == CommonComponents.GUI_DONE) {
				doneButton = widget;
				continue;
			}
			
			final int bottomY = widget.getY() + widget.getHeight();
			
			if (widget.getX() + widget.getWidth() < center) {
				leftBottomY = Math.max(leftBottomY, bottomY);
			}
			if (widget.getX() >= center) {
				rightBottomY = Math.max(rightBottomY, bottomY);
			}
		}
		
		final int x, y;
		
		if (leftBottomY <= rightBottomY) {
			x = center - 155;
			y = leftBottomY + 4;
		}
		else {
			x = center + 5;
			y = rightBottomY + 4;
		}
		
		final MutableComponent buttonTitle = BetterControlsScreen.TITLE.plainCopy().append("...");
		addRenderableWidget(Button.builder(buttonTitle, btn -> showOptionsScreen(screen)).pos(x, y).size(150, 20).build());
		
		if (doneButton != null) {
			doneButton.setY(y + 24);
		}
	}
	
	private static void showOptionsScreen(final ControlsScreen screen) {
		final Minecraft mc = Minecraft.getInstance();
		mc.setScreen(new BetterControlsScreen(mc, screen));
	}
}
