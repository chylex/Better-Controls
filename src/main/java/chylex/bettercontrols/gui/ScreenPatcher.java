package chylex.bettercontrols.gui;
import chylex.bettercontrols.mixin.AccessScreenButtons;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.Option;
import java.util.List;
import java.util.function.Consumer;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

public final class ScreenPatcher{
	private ScreenPatcher(){}
	
	public static void onControlsScreenOpened(final ControlsOptionsScreen screen){
		final AccessScreenButtons accessor = (AccessScreenButtons)screen;
		
		final List<? extends Element> children = screen.children();
		final List<AbstractButtonWidget> buttons = accessor.getButtons();
		
		final AbstractButtonWidget autoJump = buttons
			.stream()
			.filter(it -> it instanceof OptionButtonWidget && ((OptionButtonWidget)it).getOption() == Option.AUTO_JUMP)
			.findAny()
			.orElse(null);
		
		if (autoJump != null){
			children.remove(autoJump);
			buttons.remove(autoJump);
			
			accessor.callAddButton(new ButtonWidget(autoJump.x, autoJump.y, autoJump.getWidth(), autoJump.getHeight(), BetterControlsScreen.TITLE.copy().append("..."), btn -> {
				MINECRAFT.openScreen(new BetterControlsScreen(screen));
			}));
		}
	}
	
	public static void onAccessibilityScreenOpened(final AccessibilityOptionsScreen screen){
		walkChildren(screen.children(), it -> {
			if (it instanceof OptionButtonWidget){
				final OptionButtonWidget button = (OptionButtonWidget)it;
				final Option option = button.getOption();
				
				if (option == Option.SPRINT_TOGGLED || option == Option.SNEAK_TOGGLED){
					button.active = false;
				}
			}
		});
	}
	
	private static void walkChildren(final List<? extends Element> elements, final Consumer<Element> callback){
		for(final Element element : elements){
			callback.accept(element);
			
			if (element instanceof ParentElement){
				walkChildren(((ParentElement)element).children(), callback);
			}
		}
	}
}
