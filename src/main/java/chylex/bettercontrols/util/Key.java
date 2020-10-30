package chylex.bettercontrols.util;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Input;

public final class Key{
	private Key(){}
	
	public static final Input INVALID = InputMappings.INPUT_INVALID;
	
	public static boolean isUnbound(final KeyBinding binding){
		return binding.isInvalid();
	}
	
	public static boolean isPressed(final KeyBinding binding){
		return binding.isKeyDown();
	}
	
	public static boolean wasPressed(final KeyBinding binding){
		return binding.isPressed();
	}
	
	public static String getBoundKeyText(final KeyBinding binding){
		return binding.getLocalizedName();
	}
	
	public static void bind(final KeyBinding binding, final Input input){
		binding.bind(input);
	}
	
	public static String writeBinding(final KeyBinding binding){
		return binding.getTranslationKey();
	}
	
	public static void readBinding(final KeyBinding binding, final String serialized){
		bind(binding, InputMappings.getInputByName(serialized));
	}
	
	public static Input inputFromMouse(final int button){
		return InputMappings.Type.MOUSE.getOrMakeInput(button);
	}
	
	public static Input inputFromKeyboard(final int keyCode, final int scanCode){
		return InputMappings.getInputByCode(keyCode, scanCode);
	}
}
