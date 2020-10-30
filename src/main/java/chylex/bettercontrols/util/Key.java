package chylex.bettercontrols.util;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

public final class Key{
	private Key(){}
	
	public static final InputUtil.KeyCode INVALID = InputUtil.UNKNOWN_KEYCODE;
	
	public static boolean isUnbound(final KeyBinding binding){
		return binding.isNotBound();
	}
	
	public static boolean isPressed(final KeyBinding binding){
		return binding.isPressed();
	}
	
	public static boolean wasPressed(final KeyBinding binding){
		return binding.wasPressed();
	}
	
	public static String getBoundKeyText(final KeyBinding binding){
		return binding.getLocalizedName();
	}
	
	public static void bind(final KeyBinding binding, final InputUtil.KeyCode input){
		binding.setKeyCode(input);
	}
	
	public static String writeBinding(final KeyBinding binding){
		return binding.getName();
	}
	
	public static void readBinding(final KeyBinding binding, final String serialized){
		bind(binding, InputUtil.fromName(serialized));
	}
	
	public static InputUtil.KeyCode inputFromMouse(final int button){
		return InputUtil.Type.MOUSE.createFromCode(button);
	}
	
	public static InputUtil.KeyCode inputFromKeyboard(final int keyCode, final int scanCode){
		return InputUtil.getKeyCode(keyCode, scanCode);
	}
}
