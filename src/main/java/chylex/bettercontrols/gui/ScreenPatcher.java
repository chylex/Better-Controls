package chylex.bettercontrols.gui;
import chylex.bettercontrols.mixin.AccessOptionButtonWidgetOption;
import chylex.bettercontrols.mixin.AccessScreenButtons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.options.AccessibilityScreen;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.Option;
import java.util.List;

public final class ScreenPatcher{
	private ScreenPatcher(){}
	
	public static void onControlsScreenOpened(final ControlsOptionsScreen screen){
		final AccessScreenButtons accessor = (AccessScreenButtons)screen;
		
		final List<? extends Element> children = screen.children();
		final List<AbstractButtonWidget> buttons = accessor.getButtons();
		
		final AbstractButtonWidget autoJump = buttons
			.stream()
			.filter(it -> it instanceof OptionButtonWidget && ((AccessOptionButtonWidgetOption)it).getOption() == Option.AUTO_JUMP)
			.findAny()
			.orElse(null);
		
		if (autoJump != null){
			children.remove(autoJump);
			buttons.remove(autoJump);
			
			accessor.callAddButton(new ButtonWidget(autoJump.x, autoJump.y, autoJump.getWidth(), 20, BetterControlsScreen.TITLE.copy().append("...").asFormattedString(), btn -> {
				MinecraftClient.getInstance().openScreen(new BetterControlsScreen(screen));
			}));
		}
	}
	
	public static void onAccessibilityScreenOpened(final AccessibilityScreen screen){
		final AccessScreenButtons accessor = (AccessScreenButtons)screen;
		
		accessor.getButtons()
			.stream()
			.filter(it -> it instanceof OptionButtonWidget)
			.forEach(it -> {
				final OptionButtonWidget button = (OptionButtonWidget)it;
				final Option option = ((AccessOptionButtonWidgetOption)button).getOption();
				
				if (option == Option.SPRINT_TOGGLED || option == Option.SNEAK_TOGGLED){
					button.active = false;
				}
			});
	}
}
