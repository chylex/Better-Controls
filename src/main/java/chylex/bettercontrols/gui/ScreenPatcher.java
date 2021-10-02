package chylex.bettercontrols.gui;
import chylex.bettercontrols.mixin.AccessCycleButtonFields;
import chylex.bettercontrols.mixin.AccessOptionFields;
import chylex.bettercontrols.mixin.AccessScreenButtons;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

public final class ScreenPatcher {
	private ScreenPatcher() {}
	
	public static void onControlsScreenOpened(final ControlsScreen screen) {
		final AccessScreenButtons accessor = (AccessScreenButtons)screen;
		final List<? extends GuiEventListener> children = screen.children();
		
		final CycleButton<?> autoJump = children.stream().flatMap(it -> getOptionButton(it, Option.AUTO_JUMP).stream()).findAny().orElse(null);
		
		if (autoJump != null) {
			final Button widget = new Button(autoJump.x, autoJump.y, autoJump.getWidth(), autoJump.getHeight(), BetterControlsScreen.TITLE.plainCopy().append("..."), btn -> {
				MINECRAFT.setScreen(new BetterControlsScreen(screen));
			});
			
			accessor.callRemoveWidget(autoJump);
			accessor.callAddRenderableWidget(widget);
		}
	}
	
	public static void onAccessibilityScreenOpened(final AccessibilityOptionsScreen screen) {
		walkChildren(screen.children(), it -> {
			getOptionButton(it, Option.TOGGLE_SPRINT).ifPresent(button -> button.active = false);
			getOptionButton(it, Option.TOGGLE_CROUCH).ifPresent(button -> button.active = false);
		});
	}
	
	private static void walkChildren(final List<? extends GuiEventListener> elements, final Consumer<GuiEventListener> callback) {
		for (final GuiEventListener element : elements) {
			callback.accept(element);
			
			if (element instanceof ContainerEventHandler) {
				walkChildren(((ContainerEventHandler)element).children(), callback);
			}
		}
	}
	
	private static Optional<CycleButton<?>> getOptionButton(final GuiEventListener element, final Option option) {
		if (element instanceof CycleButton<?> && ((AccessOptionFields)option).getCaption().equals(((AccessCycleButtonFields)element).getName())) {
			return Optional.of((CycleButton<?>)element);
		}
		else {
			return Optional.empty();
		}
	}
}
