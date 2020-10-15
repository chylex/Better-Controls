package chylex.bettercontrols.gui;
import chylex.bettercontrols.mixin.AccessScreenButtons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.Option;
import java.util.List;

public final class ControlsScreenPatcher{
	private ControlsScreenPatcher(){}
	
	public static void onOpened(final ControlsOptionsScreen screen){
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
				MinecraftClient.getInstance().openScreen(new BetterControlsScreen(screen));
			}));
		}
	}
}
