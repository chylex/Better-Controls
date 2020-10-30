package chylex.bettercontrols.gui;
import chylex.bettercontrols.mixin.AccessScreenButtons;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.OptionButton;
import java.util.List;
import java.util.function.Consumer;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

public final class ScreenPatcher{
	private ScreenPatcher(){}
	
	public static void onControlsScreenOpened(final ControlsScreen screen){
		final AccessScreenButtons accessor = (AccessScreenButtons)screen;
		
		final List<? extends IGuiEventListener> children = screen.getEventListeners();
		final List<AbstractButton> buttons = accessor.getButtons();
		
		final AbstractButton autoJump = buttons
			.stream()
			.filter(it -> it instanceof OptionButton && ((OptionButton)it).func_238517_a_() == AbstractOption.AUTO_JUMP)
			.findAny()
			.orElse(null);
		
		if (autoJump != null){
			children.remove(autoJump);
			buttons.remove(autoJump);
			
			accessor.callAddButton(new Button(autoJump.x, autoJump.y, autoJump.getWidth(), 20, BetterControlsScreen.TITLE.copy().appendString("..."), btn -> {
				MINECRAFT.displayGuiScreen(new BetterControlsScreen(screen));
			}));
		}
	}
	
	public static void onAccessibilityScreenOpened(final AccessibilityScreen screen){
		walkChildren(screen.getEventListeners(), it -> {
			if (it instanceof OptionButton){
				final OptionButton button = (OptionButton)it;
				final AbstractOption option = button.func_238517_a_();
				
				if (option == AbstractOption.SPRINT || option == AbstractOption.SNEAK){
					button.active = false;
				}
			}
		});
	}
	
	private static void walkChildren(final List<? extends IGuiEventListener> listeners, final Consumer<IGuiEventListener> callback){
		for(final IGuiEventListener listener : listeners){
			callback.accept(listener);
			
			if (listener instanceof INestedGuiEventHandler){
				walkChildren(((INestedGuiEventHandler)listener).getEventListeners(), callback);
			}
		}
	}
}
