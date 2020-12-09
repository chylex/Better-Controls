package chylex.bettercontrols.util;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public final class Key{
	private Key(){}
	
	public static final InputUtil.Key INVALID = InputUtil.UNKNOWN_KEY;
	
	public static boolean isUnbound(final KeyBinding binding){
		return binding.isUnbound();
	}
	
	public static boolean isPressed(final KeyBinding binding){
		return binding.isPressed();
	}
	
	public static boolean wasPressed(final KeyBinding binding){
		return binding.wasPressed();
	}
	
	public static Text getBoundKeyText(final KeyBinding binding){
		return binding.getBoundKeyLocalizedText();
	}
	
	public static void bind(final KeyBinding binding, final InputUtil.Key input){
		binding.setBoundKey(input);
	}
	
	public static String writeBinding(final KeyBinding binding){
		return binding.getBoundKeyTranslationKey();
	}
	
	public static void readBinding(final KeyBinding binding, final String serialized){
		try{
			bind(binding, InputUtil.fromTranslationKey(serialized));
		}catch(final IllegalArgumentException e){
			e.printStackTrace(); // let's not crash if the config file has garbage, okay?
		}
	}
	
	public static InputUtil.Key inputFromMouse(final int button){
		return InputUtil.Type.MOUSE.createFromCode(button);
	}
	
	public static InputUtil.Key inputFromKeyboard(final int keyCode, final int scanCode){
		return InputUtil.fromKeyCode(keyCode, scanCode);
	}
}
