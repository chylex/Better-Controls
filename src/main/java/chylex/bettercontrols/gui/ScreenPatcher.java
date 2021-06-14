package chylex.bettercontrols.gui;
import chylex.bettercontrols.mixin.AccessCyclingButtonWidgetFields;
import chylex.bettercontrols.mixin.AccessOptionFields;
import chylex.bettercontrols.mixin.AccessScreenButtons;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.Option;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

public final class ScreenPatcher {
	private ScreenPatcher() {}
	
	public static void onControlsScreenOpened(final ControlsOptionsScreen screen) {
		final AccessScreenButtons accessor = (AccessScreenButtons)screen;
		final List<? extends Element> children = screen.children();
		
		final ClickableWidget autoJump = children.stream().flatMap(it -> getOptionButton(it, Option.AUTO_JUMP).stream()).findAny().orElse(null);
		
		if (autoJump != null) {
			final ButtonWidget widget = new ButtonWidget(autoJump.x, autoJump.y, autoJump.getWidth(), autoJump.getHeight(), BetterControlsScreen.TITLE.copy().append("..."), btn -> {
				MINECRAFT.openScreen(new BetterControlsScreen(screen));
			});
			
			accessor.callRemove(autoJump);
			accessor.getChildren().add(widget);
			accessor.getSelectables().add(widget);
			accessor.getDrawables().add(widget);
		}
	}
	
	public static void onAccessibilityScreenOpened(final AccessibilityOptionsScreen screen) {
		walkChildren(screen.children(), it -> {
			getOptionButton(it, Option.SPRINT_TOGGLED).ifPresent(button -> button.active = false);
			getOptionButton(it, Option.SNEAK_TOGGLED).ifPresent(button -> button.active = false);
		});
	}
	
	private static void walkChildren(final List<? extends Element> elements, final Consumer<Element> callback) {
		for (final Element element : elements) {
			callback.accept(element);
			
			if (element instanceof ParentElement) {
				walkChildren(((ParentElement)element).children(), callback);
			}
		}
	}
	
	private static Optional<CyclingButtonWidget<?>> getOptionButton(final Element element, final Option option) {
		if (element instanceof CyclingButtonWidget<?> && ((AccessOptionFields)option).getKey().equals(((AccessCyclingButtonWidgetFields)element).getOptionText())) {
			return Optional.of((CyclingButtonWidget<?>)element);
		}
		else {
			return Optional.empty();
		}
	}
}
